package com.example.StatMoney.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CryptoCompareService {

    private final RestTemplate restTemplate;
    private final String apiKey = "8146bb0e9c1cefc669a28177d4455c1ebed6c99784b26aa0bf5c294a33569bab";

    @Autowired
    public CryptoCompareService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public float getCryptoPrice(String cryptoSymbol) {
        String url = String.format(
                "https://min-api.cryptocompare.com/data/price?fsym=%s&tsyms=USD&api_key=%s",
                cryptoSymbol.toUpperCase(),
                apiKey
        );

        try {
            String response = restTemplate.getForObject(url, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response);
            return root.path("USD").floatValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
