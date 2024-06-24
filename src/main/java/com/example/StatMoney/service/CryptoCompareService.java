package com.example.StatMoney.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class CryptoCompareService {

    private final RestTemplate restTemplate;
    private final String apiKey = "8146bb0e9c1cefc669a28177d4455c1ebed6c99784b26aa0bf5c294a33569bab";
    @Autowired
    private CbrService cbrService;

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
            float priceInUSD = root.path("USD").floatValue();
            float usdToRubRate = cbrService.getCurrentCurrencyRate("USD");
            return priceInUSD * usdToRubRate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Map<String, String>> getAllCryptocurrencies() {
        String url = String.format(
                "https://min-api.cryptocompare.com/data/all/coinlist?api_key=%s",
                apiKey
        );

        List<Map<String, String>> cryptocurrencies = new ArrayList<>();

        try {
            String response = restTemplate.getForObject(url, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response).path("Data");

            Iterator<Map.Entry<String, JsonNode>> fields = root.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String symbol = field.getKey();
                String fullName = field.getValue().path("FullName").asText();

                cryptocurrencies.add(Map.of("symbol", symbol, "fullName", fullName));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cryptocurrencies;
    }
}
