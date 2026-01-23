package com.example.order_service.controller;

import com.example.order_service.model.Order;
import com.example.order_service.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin("http://frontend:5173")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping(consumes = "application/json")
    public Order placeOrder(@RequestBody Map<String, Object> request) {

        String productId = (String) request.get("productId");
        Integer quantity = (Integer) request.get("quantity");

        return service.placeOrder(productId, quantity);
    }
}
