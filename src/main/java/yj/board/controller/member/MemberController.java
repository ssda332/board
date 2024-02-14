package yj.board.controller.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import yj.board.auth.PrincipalDetails;
import yj.board.domain.member.Member;
import yj.board.domain.member.dto.MemberDto;
import yj.board.domain.member.dto.MemberInfoDto;
import yj.board.domain.member.dto.MemberUpdateDto;
import yj.board.domain.member.dto.MyPageInfoDto;
import yj.board.service.ArticleService;
import yj.board.service.CommentService;
import yj.board.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("members")
@RequiredArgsConstructor
public class MemberController {

    @Autowired
    private final MemberService memberService;
    @Autowired
    private final ArticleService articleService;
    @Autowired
    private final CommentService commentService;

    @GetMapping("")
    public String signUp() {
        return "members/register";
    }

    @GetMapping("/mypage")
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

    @PostMapping("/mypage")
    public ResponseEntity<MyPageInfoDto> myPageInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = principalDetails.getMember();
        Long id = member.getId();
        int articleCount = articleService.selectMemberArticleCount(id);
        int commentCount = commentService.selectMemberCommentCount(id);
        MemberInfoDto memberInfoDto = MemberInfoDto.from(member);
        MyPageInfoDto myPageInfoDto = MyPageInfoDto.builder()
                .articleCount(articleCount)
                .commentCount(commentCount)
                .memberInfoDto(memberInfoDto)
                .build();

        return ResponseEntity.ok(myPageInfoDto);
    }

    @PutMapping("/mypage")
    public ResponseEntity updateMember(@Valid @RequestBody MemberUpdateDto updateDto) {
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
