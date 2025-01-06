package com.jplumi.inventory_service.service;

import com.jplumi.inventory_service.dto.InventoryResponse;
import com.jplumi.inventory_service.model.Inventory;
import com.jplumi.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public List<InventoryResponse> checkStock(List<String> skuCodes) {
        return inventoryRepository.findBySkuIn(skuCodes).stream()
                .map(this::inventoryToResponse)
                .toList();
    }

    private InventoryResponse inventoryToResponse(Inventory inventory) {
        return new InventoryResponse(
                inventory.getSku(),
                inventory.getQuantity() > 0
        );
    }

}
