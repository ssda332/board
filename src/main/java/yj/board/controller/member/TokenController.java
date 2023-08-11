package yj.board.controller.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import yj.board.domain.dto.LoginDto;
import yj.board.domain.dto.TokenDto;
import yj.board.domain.member.Member;
import yj.board.jwt.JwtFilter;
import yj.board.jwt.TokenProvider;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/token")
public class TokenController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public TokenController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @GetMapping("/new")
    public String loginForm(@ModelAttribute("member") Member member) {
        return "members/login";
    }

    @ResponseBody
    @PostMapping("")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginDto loginDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getLoginId(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
//        log.debug("authentication : {}", authentication);
        String jwt = tokenProvider.createToken(authentication);
//        log.debug("jwt : {}", jwt);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        /*memberService.login(loginDto);

        토큰을 쿠키로 발급 및 응답에 추가
        Cookie cookie = new Cookie(JwtFilter.AUTHORIZATION_HEADER, jwt);
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7일 동안 유효
        cookie.setPath("/");
        cookie.setDomain("localhost");
        cookie.setSecure(false);

        response.addCookie(cookie);
        ResponseEntity res = new ResponseEntity();*/

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }

    /*@PostMapping("")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {

        log.debug("/authenticate start");

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getLoginId(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.debug("authentication : {}", authentication);

        String jwt = tokenProvider.createToken(authentication);
        log.debug("jwt : {}", jwt);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }*/
}