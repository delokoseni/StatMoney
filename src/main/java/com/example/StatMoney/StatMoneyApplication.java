package com.example.StatMoney;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.StatMoney.service.CbrService;
import com.example.StatMoney.service.MoexService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

        //Облигации ГК Самолет выпуск 11
        String securityCode = "RU000A104JQ3";
        securityPrice = moexService.getCurrentBondPrice(securityCode);
        System.out.println("Current price of " + securityCode + ": " + securityPrice);

        //Облигации Норильский Никель БО (курс в Юанях, но переведён в рубли)
        securityCode = "RU000A105NL3";
        securityPrice = moexService.getCurrentBondPrice(securityCode);
        System.out.println("Current price of " + securityCode + ": " + securityPrice);
    }
}
