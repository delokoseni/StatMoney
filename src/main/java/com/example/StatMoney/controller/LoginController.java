package com.example.StatMoney.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping
    String show()
    {
        System.out.println("Slava putinu!");
        System.out.println("Slava geroyam!");
        return "index";
    }
}
