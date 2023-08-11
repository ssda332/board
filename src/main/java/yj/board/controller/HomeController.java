package yj.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import yj.board.controller.argumentresolver.Login;
import yj.board.domain.member.Member;

@Controller
public class HomeController {
    @GetMapping("/")
    public String homeLoginModel(@Login Member loginMember, Model model) {

        /*// 세션에 회원 데이터가 없으면 members/login
        if (loginMember == null) {
            return "members/login";
        }

        // 세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "home";*/
        return "index";
    }

//    @GetMapping("/")
    public String homeLogin() {
//        return "members/login";
        return "index";
    }
}
