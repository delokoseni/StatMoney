package com.example.StatMoney.service;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import ru.exdata.moex.IssClient;
import ru.exdata.moex.IssClientBuilder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.Map;

@Service
public class MoexService {

    private final IssClient client;

    public MoexService() {
        this.client = IssClientBuilder.builder().build();
    }

    public float getCurrentPrice(String ticker) {
        try {
            // Подготовка запроса к API МосБиржи
            var response = client.iss()
                    .engines()
                    .engine("stock")
                    .markets()
                    .market("shares")
                    .securities()
                    .security(ticker)
                    .format().xml() // Изменяем формат на XML
                    .get(Map.of("limit", "1"));

            // Извлечь данные из раздела 'data' в XML
            return parsePriceFromResponse(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private float parsePriceFromResponse(String xmlResponse) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new java.io.ByteArrayInputStream(xmlResponse.getBytes("UTF-8")));
            NodeList rows = doc.getElementsByTagName("row");

            for (int i = 0; i < rows.getLength(); i++) {
                Element row = (Element) rows.item(i);
                // Предположим что LCURRENTPRICE это цена текущей линии
                String currentPriceStr = row.getAttribute("LCURRENTPRICE");
                if (currentPriceStr != null && !currentPriceStr.isEmpty()) {
                    return Float.parseFloat(currentPriceStr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
