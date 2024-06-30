package com.example.StatMoney.controller;

import com.example.StatMoney.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    String dashboard()
    {
        return "dashboard";
    }

    @GetMapping("/stats")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    String actives(Model model)
    {
        model.addAttribute(userService.findById(2L));
        return "stats";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    String addActive()
    {
        return "add-active";
    }

}
