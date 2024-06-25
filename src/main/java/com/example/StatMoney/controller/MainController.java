package com.example.StatMoney.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/main")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    String mainPage()
    {
        return "main-page";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    String addActive()
    {
        return "add-active";
    }

    @GetMapping("/actives")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    String actives()
    {
        return "actives";
    }
}
