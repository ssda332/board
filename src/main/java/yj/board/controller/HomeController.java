package yj.board.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import yj.board.controller.argumentresolver.Login;
import yj.board.domain.member.Member;

@Slf4j
@Controller
public class HomeController {
    @GetMapping("/")
    public String homeLoginModel(@Login Member loginMember, Model model) {
        log.debug("url = /, homeLoginModel");
        return "index";
    }

//    @GetMapping("/")
    public String homeLogin() {
//        return "members/login";
        return "index";
    }
}
