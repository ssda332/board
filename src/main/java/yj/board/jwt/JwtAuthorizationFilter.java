package yj.board.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import yj.board.auth.PrincipalDetails;
import yj.board.domain.member.Member;
import yj.board.repository.MemberRepositoryV2;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter{

    private MemberRepositoryV2 memberRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepositoryV2 memberRepository) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader(JwtProperties.HEADER_STRING);
        if(header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String token = request.getHeader(JwtProperties.HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX, "");

        String loginId = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
                .getClaim("loginId").asString();

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

            // 권한 관리를 위해 세션에 접근하여 값 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

}
