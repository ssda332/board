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
import yj.board.domain.dto.MemberDto;
import yj.board.domain.member.Member;
import yj.board.service.MemberService;
import yj.board.service.MemberServiceV2;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("members")
@RequiredArgsConstructor
public class MemberController {

    @Autowired
    private final MemberServiceV2 memberService;



    @GetMapping("")
    public String signUp(@ModelAttribute("member") Member member) {

        return "members/register";
    }

    @PostMapping("")
    public String save(@Validated @ModelAttribute("member") MemberDto memberDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.info("로그인 에러");
            return "members/register";
        }

        log.info(memberDto.toString());

        try {
            MemberDto signup = memberService.signup(memberDto);
        } catch (RuntimeException e) {
            FieldError fieldError = new FieldError("member", "loginId", "이미 존재하는 아이디입니다.");
            bindingResult.addError(fieldError);
            return "members/register";
        }

        /*if (result == 2) {
            FieldError fieldError = new FieldError("member", "loginId", "이미 존재하는 아이디입니다.");
            bindingResult.addError(fieldError);
            return "members/register";
        }*/

        return "redirect:/";

    }

}
