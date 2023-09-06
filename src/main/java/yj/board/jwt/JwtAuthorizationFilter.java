package yj.board.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import yj.board.domain.token.dto.TokenDto;
import yj.board.repository.MemberRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter{

    private MemberRepository memberRepository;
    private TokenProvider tokenProvider;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository, TokenProvider tokenProvider) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        log.debug("JwtAuthorizationFilter init url : {}", request.getRequestURL());

        String header = request.getHeader(JwtProperties.HEADER_STRING);
        log.debug("header = {}", header);
        if(header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            log.debug("유효한 JWT 토큰이 없습니다, uri: {}", request.getRequestURL());
            chain.doFilter(request, response); // Access Token 없으면 통과
            return;
        }

        String accessToken = request.getHeader(JwtProperties.HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX, "");

        if (!tokenProvider.validationToken(accessToken)) {
            // JWTVerificationException
            // 리프레쉬토큰 유효성 검사 -> 액세스토큰 재발급
//            accessToken = tokenProvider.reissue(accessToken, response);
            TokenDto tokenDto = tokenProvider.reissue(accessToken, request, response);
            accessToken = tokenDto.getAccessToken();

//            response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);
        }

        // 권한 관리를 위해 세션에 접근하여 값 저장
        Authentication authentication = tokenProvider.getAuthentication(accessToken, response);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }

}
