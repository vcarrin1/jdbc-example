package com.vcarrin87.jdbc_example.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vcarrin87.jdbc_example.models.Inventory;
import com.vcarrin87.jdbc_example.repository.InventoryRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    /**
     * Creates a new inventory record.
     */
    public void createInventoryRecord(Inventory inventory) {
        inventoryRepository.save(inventory);
        log.info("Inventory record created: {}", inventory);
    }

    /**
     * Get inventory by product ID.
     */
    public Inventory getInventoryByProductId(int productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId);
        if (inventory != null) {
            log.info("Inventory found for product ID {}: {}", productId, inventory);
        } else {
            log.warn("No inventory found for product ID {}", productId);
        }
        return inventory;
    }
}
