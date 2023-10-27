package yj.board.controller.token;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import yj.board.domain.member.Member;
import yj.board.domain.member.dto.LoginDto;
import yj.board.domain.token.dto.ReissueTokenDto;
import yj.board.domain.token.dto.TokenDto;
import yj.board.jwt.JwtProperties;
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

    @GetMapping("/new")
    public String loginForm(@ModelAttribute("member") Member member) {
        return "members/login";
    }

    @PostMapping("")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginDto loginDto, HttpServletResponse response) {
        TokenDto tokenDto = tokenService.login(loginDto);
        responseTokenDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken(), response);

        response.addCookie(new Cookie("testCookie", "testCookie"));

        return ResponseEntity.ok(tokenDto);
    }

    @PutMapping("")
    public ResponseEntity<ReissueTokenDto> reissue(@RequestBody TokenDto tokenDto, HttpServletResponse response) {
        ReissueTokenDto reissueDto = tokenService.reissue(tokenDto);
        responseTokenDto(reissueDto.getAccessToken(), reissueDto.getRefreshToken(), response);

        return ResponseEntity.ok(reissueDto);
    }

    public void responseTokenDto(String accessToken, String refreshToken, HttpServletResponse response) {
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);
        response.addHeader(JwtProperties.REFRESH_HEADER_STRING, JwtProperties.TOKEN_PREFIX + refreshToken);
    }

}