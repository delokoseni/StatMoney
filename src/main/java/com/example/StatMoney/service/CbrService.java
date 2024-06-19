package com.example.StatMoney.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class CbrService {

    private final RestTemplate restTemplate;

    @Autowired
    public CbrService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getCurrentCurrencyRate(String currencyCode) {
        String url = "https://www.cbr.ru/scripts/XML_daily.asp?date_req=" + getCurrentDate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        return responseEntity.getBody();
    }

    private String getCurrentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.now().format(formatter);
    }
}
