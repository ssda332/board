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
import yj.board.auth.PrincipalDetails;
import yj.board.domain.member.dto.MemberDto;
import yj.board.domain.member.dto.MemberInfoDto;
import yj.board.domain.token.dto.TokenDto;
import yj.board.domain.member.Member;
import yj.board.repository.MemberRepository;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final MemberRepository memberRepository;

    // 토큰 생성
    public TokenDto createToken(MemberInfoDto memberInfoDto) {

        String accessToken = JWT.create()
                .withSubject(memberInfoDto.getLoginId())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        String refreshToken = JWT.create()
                .withSubject(memberInfoDto.getLoginId())
                .withClaim("memId", memberInfoDto.getId())
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

    public Authentication getAuthentication(String accessToken) {
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
}
