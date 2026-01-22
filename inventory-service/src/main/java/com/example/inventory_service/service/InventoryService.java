package com.example.inventory_service.service;

import com.example.inventory_service.model.Inventory;
import com.example.inventory_service.repository.InventoryRepository;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final InventoryRepository repository;

    public InventoryService(InventoryRepository repository) {
        this.repository = repository;
    }

    public Inventory getInventoryByProductId(String productId) {
        return repository.findByProductId(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found: " + productId)
                );
    }
}
