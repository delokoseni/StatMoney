package com.example.StatMoney;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.StatMoney.service.CbrService;
import com.example.StatMoney.service.MoexService;
import com.example.StatMoney.service.CryptoCompareService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class StatMoneyApplication implements CommandLineRunner {

    @Autowired
    private CbrService cbrService;

    @Autowired
    private MoexService moexService;

    @Autowired
    private CryptoCompareService cryptoCompareService;

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

        String cryptoSymbol = "BTC";
        float cryptoPrice = cryptoCompareService.getCryptoPrice(cryptoSymbol);
        System.out.println("Current price of " + cryptoSymbol + ": " + cryptoPrice);

        cryptoSymbol = "1INCH";
        cryptoPrice = cryptoCompareService.getCryptoPrice(cryptoSymbol);
        System.out.println("Current price of " + cryptoSymbol + ": " + cryptoPrice);

        //Получение списка всех криптовалют
        List<Map<String, String>> cryptocurrencies = cryptoCompareService.getAllCryptocurrencies();
        System.out.println("All cryptocurrencies:");
        for (Map<String, String> crypto : cryptocurrencies) {
            System.out.println(crypto.get("fullName") + " - " + crypto.get("symbol"));
        }

        //Все акции с Мосбиржи
        List<Map<String, String>> shares = moexService.getAllSecurities("shares");
        System.out.println("All shares:");
        for (Map<String, String> sh : shares) {
            System.out.println(sh.get("name") + " - " + sh.get("ticker"));
        }

        //Все облигации с мосбиржи
        List<Map<String, String>> bonds = moexService.getAllSecurities("bonds");
        System.out.println("All bonds:");
        for (Map<String, String> bd : bonds) {
            System.out.println(bd.get("name") + " - " + bd.get("ticker"));
        }
    }
}
