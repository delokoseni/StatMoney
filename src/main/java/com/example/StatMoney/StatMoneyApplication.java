package com.example.StatMoney;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.StatMoney.service.CbrService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.exdata.moex.IssClientBuilder;

@SpringBootApplication
public class StatMoneyApplication implements CommandLineRunner {

    @Autowired
    private CbrService cbrService;

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

        // Пример использования API MOEX
        // var client = IssClientBuilder.builder().build();
        // var str = client.iss().index().format().json().get();
        // System.out.println(str);

        // Другой пример использования API MOEX с параметрами
        // var client = IssClientBuilder.builder().build();
        // var history = client
        //         .iss()
        //         .history()
        //         .engines()
        //         .engine("stock")
        //         .markets()
        //         .market("shares")
        //         .boards()
        //         .board("TQBR")
        //         .securities()
        //         .security("sber")
        //         .format().json() // Выбираем формат JSON
        //         .get(Map.of("from", "2020-01-03", "till", "2020-10-03")); // Задаем параметры и выполняем запрос
        // System.out.println(history);
    }
}
