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

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private MemberRepositoryV2 memberRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepositoryV2 memberRepository) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        //String header = request.getHeader(JwtProperties.HEADER_STRING);
        log.debug("authorization filter init");

        String token = null;
        try {
            token = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(JwtProperties.HEADER_STRING)).findFirst().map(Cookie::getValue)
                    .orElse(null);
        }catch (Exception e){

        }


        if(token == null || !token.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        /*String token = request.getHeader(JwtProperties.HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX, "");*/
        token = token.replace(JwtProperties.TOKEN_PREFIX, "");

        String loginId = null;
        try {
                loginId = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
                    .getClaim("loginId").asString();
                request.setAttribute("loginid",loginId);
            } catch(JWTVerificationException e) {
                // 토큰 검증 실패 예외(만료, decode실패 등등)
                // 1. 쿠키 제거
                Cookie jwtCookie = new Cookie(JwtProperties.HEADER_STRING, null);
                jwtCookie.setMaxAge(0); // 쿠키 만료시간을 0으로 설정하여 삭제
                jwtCookie.setPath("/");
                response.addCookie(jwtCookie);
                throw e;
             /*logger.info("만료된 JWT 토큰입니다.");

                // 토큰이 만료되었을 때 처리
                // 1. 쿠키 제거
                Cookie jwtCookie = new Cookie(JwtProperties.HEADER_STRING, null);
                jwtCookie.setMaxAge(0); // 쿠키 만료시간을 0으로 설정하여 삭제
                jwtCookie.setPath("/");
                response.addCookie(jwtCookie);

                // 2. 로그인 화면으로 리다이렉트
                response.sendRedirect("/token/new"); // 로그인 화면 URL에 맞게 수정

                return;*/

            }
        if(loginId != null) {
            request.setAttribute("loginok",true);
            Member member = memberRepository.findOneWithAuthoritiesByLoginId(loginId).get();

            PrincipalDetails principalDetails = new PrincipalDetails(member);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            principalDetails,
                            null, // 패스워드는 모르니까 null 처리, 인증 용도 x
                            principalDetails.getAuthorities());

            // 권한 관리를 위해 세션에 접근하여 값 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

}