package yj.board.controller.token;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import yj.board.domain.member.Member;
import yj.board.domain.member.dto.LoginDto;
import yj.board.domain.token.dto.ReissueTokenDto;
import yj.board.domain.token.dto.TokenDto;
import yj.board.util.JwtProperties;
import yj.board.service.TokenService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @Autowired
    private JwtProperties jwtProperties;

    @GetMapping("/new")
    public String loginForm(@ModelAttribute("member") Member member) {
        return "members/login";
    }

    @PostMapping("")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginDto loginDto, HttpServletResponse response) {
        TokenDto tokenDto = tokenService.login(loginDto);
        responseTokenDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken(), response);

        return ResponseEntity.ok(tokenDto);
    }

    @PutMapping("")
    public ResponseEntity<ReissueTokenDto> reissue(@RequestBody String accessToken, HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = null;

        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(jwtProperties.getRefreshTokenHeader())) {
                refreshToken = cookie.getValue();
            }
        }

        TokenDto tokenDto = TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        ReissueTokenDto reissueDto = tokenService.reissue(tokenDto);
        responseTokenDto(reissueDto.getAccessToken(), reissueDto.getRefreshToken(), response);

        return ResponseEntity.ok(reissueDto);
    }

    public void responseTokenDto(String accessToken, String refreshToken, HttpServletResponse response) {
        response.addHeader(jwtProperties.getHeader(), accessToken);
        Cookie cookie = getRefreshTokenCookie(refreshToken);

        response.addCookie(cookie);
    }

    public Cookie getRefreshTokenCookie(String refreshToken) {
        Cookie cookie = new Cookie(jwtProperties.getRefreshTokenHeader(), refreshToken);

        String serverDomain = System.getenv("SERVER_DOMAIN");
        if (!(serverDomain == null || serverDomain.isEmpty())) {
            // 개발서버일경우, secure설정
            cookie.setSecure(true);
        }
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(jwtProperties.getRefreshTokenValidityInSeconds());
        return cookie;
    }

}