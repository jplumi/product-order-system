package com.jplumi.inventory_service.service;

import com.jplumi.inventory_service.dto.InventoryResponse;
import com.jplumi.inventory_service.model.Inventory;
import com.jplumi.inventory_service.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    @Autowired
    private InventoryService inventoryService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void checkStock_WhenStockAvailable_ReturnInventories() {
        // Arrange
        List<String> skuCodes = Arrays.asList("123", "456");
        List<Inventory> inventories = Arrays.asList(
                new Inventory(1L, "123", 1),
                new Inventory(2L, "456", 0)
        );
        when(inventoryRepository.findBySkuIn(skuCodes)).thenReturn(inventories);

        List<InventoryResponse> expectedResponse = Arrays.asList(
                new InventoryResponse("123", true),
                new InventoryResponse("456", false)
        );

        // Act
        List<InventoryResponse> response = inventoryService.checkStock(skuCodes);

        // Assert
        verify(inventoryRepository, times(1)).findBySkuIn(skuCodes);
        assertEquals(2, response.size());

        assertEquals("123", response.get(0).getSku());
        assertTrue(response.get(0).isInStock());

        assertEquals("456", response.get(1).getSku());
        assertFalse(response.get(1).isInStock());
    }
}