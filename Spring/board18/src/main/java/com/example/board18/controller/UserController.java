package com.example.board18.controller;

import com.example.board18.entity.User;
import com.example.board18.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {

        return "/account/login";
    }

    @GetMapping("/register")
    public String register() {

        return "/account/SignUp";
    }

    @PostMapping("/register")
    public String register(User user) {

        userService.save(user);
        return "redirect:/boardList";
    }

}
