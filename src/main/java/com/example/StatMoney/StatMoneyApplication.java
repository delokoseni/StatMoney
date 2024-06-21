package com.example.StatMoney;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.StatMoney.service.CbrService;
import com.example.StatMoney.service.MoexService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.exdata.moex.IssClientBuilder;

import java.util.Map;

@SpringBootApplication
public class StatMoneyApplication implements CommandLineRunner {

    @Autowired
    private CbrService cbrService;

    @Autowired
    private MoexService moexService;

    public static void main(String[] args) {
        SpringApplication.run(StatMoneyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(5);
        String encodePass = passwordEncoder.encode("maksim");
        System.out.println("Encoded password: " + encodePass);
        String encodePass2 = passwordEncoder.encode("maksim");
        System.out.println("Encoded password: " + encodePass2);

        float cnyRate = cbrService.getCurrentCurrencyRate("CNY");
        System.out.println("Current CNY rate: " + cnyRate);

        float usdRate = cbrService.getCurrentCurrencyRate("USD");
        System.out.println("Current USD rate: " + usdRate);

        String ticker = "WUSH";
        float securityPrice = moexService.getCurrentPrice(ticker);
        System.out.println("Current price of " + ticker + ": " + securityPrice);

        ticker = "SBER";
        securityPrice = moexService.getCurrentPrice(ticker);
        System.out.println("Current price of " + ticker + ": " + securityPrice);

    }
}
