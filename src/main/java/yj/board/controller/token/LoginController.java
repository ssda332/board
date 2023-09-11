package yj.board.controller.token;

import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import yj.board.domain.member.Member;
import yj.board.domain.member.dto.LoginDto;
import yj.board.domain.token.RefreshToken;
import yj.board.domain.token.dto.TokenDto;
import yj.board.jwt.JwtProperties;
import yj.board.service.LoginService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.Instant;
import java.util.Arrays;

@Slf4j
@Controller
@RequestMapping("/token")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/new")
    public String loginForm(@ModelAttribute("member") Member member) {
        return "members/login";
    }

    @PostMapping("")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(loginService.login(loginDto, request, response));
    }

    @PutMapping("")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenDto tokenDto, HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(loginService.reissue(tokenDto, request, response));
    }

}