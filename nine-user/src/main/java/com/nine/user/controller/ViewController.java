package com.nine.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

    @RequestMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        return "login";
    }

    @RequestMapping("/home")
    public String home(HttpServletRequest request, Model model) {
        return "home";
    }


}
