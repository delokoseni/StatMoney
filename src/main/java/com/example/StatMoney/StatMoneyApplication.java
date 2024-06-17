package com.example.StatMoney;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.exdata.moex.IssClientBuilder;

import java.util.Map;

@SpringBootApplication
public class StatMoneyApplication implements CommandLineRunner {

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
		//Апи работает, но я не ебу че нам нужно из него доставать

		//var client = IssClientBuilder.builder().build();
		//var str = client.iss().index().format().json().get();
		//System.out.println(str);

//		var client = IssClientBuilder.builder().build();
//		var history = client
//				.iss()
//				.history()
//				.engines()
//				.engine("stock")
//				.markets()
//				.market("shares")
//				.boards()
//				.board("TQBR")
//				.securities()
//				.security("sber")
//				.format().json() // Выбираем формат JSON
//				.get(Map.of("from", "2020-01-03", "till", "2020-10-03")); // Задаем параметры и выполняем запрос
//		System.out.println(history);

	}
}

