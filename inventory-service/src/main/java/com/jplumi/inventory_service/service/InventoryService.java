package com.jplumi.inventory_service.service;

import com.jplumi.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public boolean checkStock(String sku) {
        return inventoryRepository.findBySku(sku).isPresent();
    }

}
