package yj.board.controller.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yj.board.domain.member.dto.MemberDto;
import yj.board.domain.member.Member;
import yj.board.service.MemberService;

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

    @PostMapping("")
    public ResponseEntity<MemberDto> signup(
            @Validated @RequestBody MemberDto memberDto) {
        return ResponseEntity.ok(memberService.signup(memberDto));
    }

}
