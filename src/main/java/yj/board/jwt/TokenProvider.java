package yj.board.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import yj.board.auth.PrincipalDetails;
import yj.board.domain.dto.TokenDto;
import yj.board.domain.member.RefreshToken;
import yj.board.repository.RefreshTokenRepository;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final RefreshTokenRepository tokenRepository;

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
    }

}
