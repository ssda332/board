package yj.board.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.CookieGenerator;
import yj.board.auth.PrincipalDetails;
import yj.board.domain.token.dto.TokenDto;
import yj.board.domain.member.Member;
import yj.board.domain.token.RefreshToken;
import yj.board.repository.MemberRepository;
import yj.board.repository.RefreshTokenRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final RefreshTokenRepository tokenRepository;
    private final MemberRepository memberRepository;

    // jwt 생성
    @Transactional
    public TokenDto createAccessToken(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject(principalDetailis.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
                .withClaim("id", principalDetailis.getMember().getId())
                .withClaim("loginId", principalDetailis.getMember().getLoginId())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        String refToken = JWT.create()
                .withSubject(principalDetailis.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME_REFRESH))
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        RefreshToken refreshToken = RefreshToken.builder()
                .memId(principalDetailis.getMember().getId())
                .refreshToken(refToken)
                .build();

        // 리프레쉬 토큰 DB저장
        tokenRepository.save(refreshToken);

        // 토큰DTO -> 클라이언트
        TokenDto tokenDto = new TokenDto(jwtToken, refToken);
        responseTokenDto(tokenDto, request, response);

        return tokenDto;
    }

    // v2
    public TokenDto createToken(Member member) {

        String accessToken = JWT.create()
                .withSubject(member.getLoginId())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
                .withClaim("id", member.getId())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        String refreshToken = JWT.create()
                .withSubject(member.getLoginId())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME_REFRESH))
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        return new TokenDto(accessToken, refreshToken);
    }

    public String resolveToken(String token) {
        if (token != null && !token.equals("null") && !token.equals("Bearer null")) {
            token = token.replace(JwtProperties.TOKEN_PREFIX, "");
            return token;
        }

        return null;
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader(JwtProperties.REFRESH_HEADER_STRING);

        if (refreshToken != null) {
            refreshToken = refreshToken.replace(JwtProperties.TOKEN_PREFIX, "");
        }

        return refreshToken;
    }

    public Authentication getAuthentication(String accessToken, HttpServletResponse response) {
        DecodedJWT decodedJWT = decodedJWT(accessToken);
        String loginId = decodedJWT.getSubject();

        Member member = memberRepository.findOneWithAuthoritiesByLoginId(loginId).get();

        Authentication authentication = getAuthentication(member);

        return authentication;

    }

    public Authentication getAuthentication(Member member) {
        PrincipalDetails principalDetails = new PrincipalDetails(member);

        String authoritiesStr = principalDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // 권한 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(authoritiesStr.split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        principalDetails,
                        null, // 패스워드는 모르니까 null 처리, 인증 용도 x
                        authorities);
        return authentication;
    }

    /**
     * 토큰 디코딩
     */

    public DecodedJWT decodedJWT(String token) {
        try {
            DecodedJWT verify = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token);

            return verify;
        } catch (JWTVerificationException e) {
            String expiredToken = token; // 만료된 토큰
            DecodedJWT decodedExpiredToken = JWT.decode(expiredToken); // 토큰 디코딩
            return decodedExpiredToken;
        }
    }

    /**
     * 액세스 토큰 재발급
     */
    public TokenDto reissue(String accessToken, HttpServletRequest request, HttpServletResponse response) {
        DecodedJWT decodedJWT = decodedJWT(accessToken);
        Long memId = decodedJWT.getClaim("id").asLong();
        String loginId = decodedJWT.getClaim("loginId").asString();

        RefreshToken refreshToken = tokenRepository.findById(memId).get();

        log.debug("reissue memid : {}, loginId : {}", memId, loginId);
        log.debug("refreshToken : {}", refreshToken.getRefreshToken());

        // 쿠키에서 refresh token 꺼내기
        String cookieRefreshToken = null;
        try {
            cookieRefreshToken = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(JwtProperties.REFRESH_HEADER_STRING)).findFirst().map(Cookie::getValue)
                    .orElse(null);
        } catch (NullPointerException e) {
            // redirect
            log.error("refresh token expired");
            throw new TokenExpiredException("refresh token expired {}", Instant.now());
        }

        if (validationToken(refreshToken.getRefreshToken()) && refreshToken.getRefreshToken().equals(cookieRefreshToken)) {
            Member member = memberRepository.findOneWithAuthoritiesByLoginId(loginId).get();
            Authentication authentication = getAuthentication(member);

            return createAccessToken(authentication, request, response);
        } else {
            // redirect
            log.error("refresh token expired");
            throw new TokenExpiredException("refresh token expired {}", Instant.now());
        }

    }

    /**
     * 토큰 검증
     */

    public boolean validationToken(String token) {
        try {
            DecodedJWT verify = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token);
            return true;
        } catch (TokenExpiredException e) {
            log.error("토큰 검증 실패 - 만료 TokenExpiredException");
        } catch (JWTVerificationException e) {
            log.error("토큰 검증 실패");
        }
        return false;
    }


    public void responseTokenDto(TokenDto tokenDto, HttpServletRequest request, HttpServletResponse response) {
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();

        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);
        response.addHeader(JwtProperties.REFRESH_HEADER_STRING, JwtProperties.TOKEN_PREFIX + refreshToken);

        /*Cookie[] cookies = request.getCookies();
        if (cookies != null) { // 쿠키가 한개라도 있으면 실행
            for (int i = 0; i < cookies.length; i++) {
                cookies[i].setMaxAge(0); // 유효시간을 0으로 설정
                response.addCookie(cookies[i]); // 응답에 추가하여 만료시키기.
            }
        }*/

        /*CookieGenerator cg = new CookieGenerator();
        cg.setCookieName(JwtProperties.REFRESH_HEADER_STRING);
        cg.setCookieHttpOnly(true);
        cg.setCookieSecure(true);
        cg.setCookieMaxAge(JwtProperties.EXPIRATION_TIME_REFRESH / 1000);
        log.debug("cookie maxAge : {}", JwtProperties.EXPIRATION_TIME_REFRESH / 1000);
        cg.addCookie(response, refreshToken);*/
    }

}
