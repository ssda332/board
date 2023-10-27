package yj.board.controller.member;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yj.board.domain.member.dto.MemberDto;
import yj.board.domain.member.Member;
import yj.board.domain.member.dto.MemberUpdateDto;
import yj.board.domain.token.dto.TokenDto;
import yj.board.jwt.JwtProperties;
import yj.board.jwt.TokenProvider;
import yj.board.service.MemberService;
import yj.board.service.TokenService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("members")
@RequiredArgsConstructor
public class MemberController {

    @Autowired
    private final MemberService memberService;

    @GetMapping("")
    public String signUp() {
        return "members/register";
    }

    @GetMapping("/member/mypage")
    public String myPage() {
        return "members/mypage";
    }

    @PostMapping("")
    public ResponseEntity<MemberDto> signup(
            @Valid @RequestBody MemberDto memberDto) {
        return ResponseEntity.ok(memberService.signup(memberDto));
    }

    @GetMapping("/member")
//    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<MemberDto> getMyUserInfo(HttpServletRequest request) {
        return ResponseEntity.ok(memberService.getMyUserWithAuthorities());
    }

    @PutMapping("/member/mypage")
//    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity updateMember(@Valid @RequestBody MemberUpdateDto updateDto) {
//        return ResponseEntity.ok(memberService.getMyUserWithAuthorities());
        memberService.updateMember(updateDto);
        return ResponseEntity.ok("ok");
    }

    /*private static Claim getDecodedJWT(String token) {
        DecodedJWT decodedJWT;

        if (token != null && !token.equals("null") && !token.equals("Bearer null")) {
            token = token.replace(JwtProperties.TOKEN_PREFIX, "");
        }

        try {
            decodedJWT = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token);
        } catch (JWTVerificationException e) {
            String expiredToken = token; // 만료된 토큰
            decodedJWT = JWT.decode(expiredToken); // 토큰 디코딩
        }

        return decodedJWT.getClaim("memId");
    }*/

}
