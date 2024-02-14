package yj.board.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import yj.board.jwt.JwtProperties;
import yj.board.jwt.TokenProvider;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtFilter extends GenericFilterBean {

    private final TokenProvider tokenProvider;

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {

        /**
         * 1. request 에서 token 을 취한다.
         */
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        String bearerToken = httpServletRequest.getHeader(JwtProperties.HEADER_STRING);
        String token = tokenProvider.resolveToken(bearerToken);

        log.info("[Verifying token]");
        log.info(((HttpServletRequest) request).getRequestURL().toString());

        /**
         * 2. validationToken 메소드로 토큰 검증 -> SecurityContextHolder에 권한정보 set
         */
        if (token != null && tokenProvider.validationToken(token)) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        
        filterChain.doFilter(request, response);
    }
}
