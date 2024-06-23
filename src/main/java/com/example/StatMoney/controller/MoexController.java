package com.example.StatMoney.controller;

import com.example.StatMoney.service.MoexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/moex")
public class MoexController {

    @Autowired
    private MoexService moexService;

    @GetMapping("/price/{ticker}")
    public float getSecurityPrice(@PathVariable String ticker) {
        return moexService.getCurrentPrice(ticker);
    }
}
