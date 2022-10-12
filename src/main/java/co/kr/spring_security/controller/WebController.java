package co.kr.spring_security.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class WebController {

    @RequestMapping("/")
    public String mainPage() {
        return "page/index";
    }

    @RequestMapping("/signUp")
    public String signUpPage() {
        return "page/signUp";
    }

    @RequestMapping("/signIn")
    public String signInPage() {
        return "page/signIn";
    }

    @RequestMapping("/auth")
    public String connotPage() {
        return "page/auth";
    }
}
