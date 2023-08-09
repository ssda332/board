package yj.board.controller.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import yj.board.domain.member.Member;
import yj.board.service.MemberService;

@Slf4j
@Controller
@RequestMapping("members")
@RequiredArgsConstructor
public class MemberController {

    @Autowired
    private final MemberService memberService;

    @GetMapping("add")
    public String signUp(@ModelAttribute("member") Member member) {
        return "members/register";
    }

    @PostMapping("add")
    public String save(@Validated @ModelAttribute Member member, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.info("로그인 에러");
            return "members/register";
        }

        System.out.println(member.toString());

        log.info(member.toString());

        int result = memberService.save(member);

        if (result == 2) {
            FieldError fieldError = new FieldError("member", "loginId", "이미 존재하는 아이디입니다.");
            bindingResult.addError(fieldError);
            return "members/register";
        }

        return "redirect:/";

    }
}
