package com.assignment.commute.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/")
    public String home(ModelAndView mav) {
        return "home";
    }

    @ResponseBody
    @RequestMapping("/test")
    public String test() {
        return "OK";
    }

    @ResponseBody
    @RequestMapping("/adminOnly")
    public String adminOnly() {
        return "Secret Page";
    }

    @RequestMapping("/login")
    public String loginForm() {
        return "login-test";
    }




}
