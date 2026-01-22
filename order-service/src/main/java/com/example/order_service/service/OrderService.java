package com.example.order_service.service;

import com.example.order_service.client.InventoryClient;
import com.example.order_service.model.Order;
import com.example.order_service.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final InventoryClient inventoryClient;

    public OrderService(OrderRepository repository,
                        InventoryClient inventoryClient) {
        this.repository = repository;
        this.inventoryClient = inventoryClient;
    }

    public Order placeOrder(String productId, Integer quantity) {
        System.out.println("productId: "+productId);
        System.out.println("quantity: "+quantity);
        Integer available =
                inventoryClient.getAvailableQuantity(productId);
        System.out.println("available: "+available);

        if (available < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        Order order =
                new Order(productId, quantity, "PLACED");
        System.out.println("order: "+order);

        return repository.save(order);
    }
}
