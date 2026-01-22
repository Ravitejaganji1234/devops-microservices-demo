package com.example.order_service.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class InventoryClient {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String INVENTORY_URL =
//            "http://localhost:8081/inventory/{productId}";
              "http://host.docker.internal:8081/inventory/{productId}";

    public Integer getAvailableQuantity(String productId) {

        Map response = restTemplate.getForObject(
                INVENTORY_URL,
                Map.class,
                productId
        );

        return (Integer) response.get("availableQuantity");
    }
}
