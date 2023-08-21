package yj.board.controller.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import yj.board.domain.member.Member;

@Slf4j
@Controller
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {

    @GetMapping("/new")
    public String loginForm(@ModelAttribute("member") Member member) {
        return "members/login";
    }

//    @PostMapping("")
    /*public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {

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