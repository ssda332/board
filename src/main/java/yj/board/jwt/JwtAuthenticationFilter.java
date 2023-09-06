package yj.board.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import yj.board.auth.PrincipalDetails;
import yj.board.domain.member.dto.LoginDto;
import yj.board.domain.token.dto.TokenDto;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, TokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.setFilterProcessesUrl(JwtProperties.AUTH_URL);
    }

    // 인증 요청시에 실행되는 함수 => /login
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        log.debug("JwtAuthenticationFilter init url : {}", request.getRequestURL());

        // request에 있는 username과 password를 파싱해서 자바 Object로 받기
        ObjectMapper om = new ObjectMapper();
        LoginDto loginRequestDto = null;
        try {
            loginRequestDto = om.readValue(request.getInputStream(), LoginDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 유저네임패스워드 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getLoginId(),
                        loginRequestDto.getPassword());

        Authentication authentication =
                authenticationManager.authenticate(authenticationToken);

        PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();
        return authentication;
    }

    // Token 생성해서 response에 담아주기
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        TokenDto tokenDto = tokenProvider.createAccessToken(authResult, request, response);

        try {

        } catch (NullPointerException e) {
            Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(JwtProperties.REFRESH_HEADER_STRING))
                    .forEach(s -> {
                        log.debug("cookie : " + s.getValue());
                    });
        }
    }


}
