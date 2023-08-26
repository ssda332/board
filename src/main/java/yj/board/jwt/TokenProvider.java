package yj.board.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import yj.board.auth.PrincipalDetails;
import yj.board.domain.dto.TokenDto;
import yj.board.domain.member.Member;
import yj.board.domain.member.RefreshToken;
import yj.board.repository.MemberRepositoryV2;
import yj.board.repository.RefreshTokenRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final RefreshTokenRepository tokenRepository;
    private final MemberRepositoryV2 memberRepository;

    // jwt 생성
    @Transactional
    public TokenDto createAccessToken(Authentication authentication) {
        PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject(principalDetailis.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
                .withClaim("id", principalDetailis.getMember().getId())
                .withClaim("loginId", principalDetailis.getMember().getLoginId())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        String refToken = JWT.create()
                .withSubject(principalDetailis.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        RefreshToken refreshToken = RefreshToken.builder()
                .memId(principalDetailis.getMember().getId())
                .refreshToken(refToken)
                .build();

        tokenRepository.save(refreshToken);

        TokenDto tokenDto = TokenDto.builder()
                .accessToken(jwtToken)
                .refreshToken(refToken)
                .build();

        return tokenDto;
    }


    public Authentication getAuthentication(String token) {
        String loginId = validationToken(token);

        if(loginId != null) {
            Member member = memberRepository.findOneWithAuthoritiesByLoginId(loginId).get();
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
        } else {

        }

        return null;

    }
/*
    // Jwt 로 인증정보를 조회
    public Authentication getAuthentication(String token) {

        // Jwt 에서 claims 추출
        Claims claims = parseClaims(token);

        // 권한 정보가 없음
        if (claims.get(ROLES) == null) {
            throw new CAuthenticationEntryPointException();
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Jwt 토큰 복호화해서 가져오기
    private String parseToken(String token) {
        try {

            String loginId = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
                    .getClaim("loginId").asString();

            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }*/

    public String validationToken(String token) {
        try {
            String loginId = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
                    .getClaim("loginId").asString();
            return loginId;
        } catch(JWTVerificationException e) {
            log.debug("JwtAuthorizationFilter error : {}", e.getMessage());
            // refresh token 검증

        }

        return null;
    }
}
