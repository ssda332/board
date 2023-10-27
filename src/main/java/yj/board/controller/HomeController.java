package yj.board.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import yj.board.domain.member.Member;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
public class HomeController {
    @GetMapping("/")
    public String homeLoginModel(Member loginMember, Model model, HttpServletRequest request) {
        log.debug("url = /, homeLoginModel");

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String cookieValue = cookie.getValue();

                log.debug("cookieName : {}", cookie.getName());
            }
        }

        return "index";
    }


}
