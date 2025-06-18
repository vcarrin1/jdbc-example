package com.vcarrin87.jdbc_example.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vcarrin87.jdbc_example.models.Inventory;
import com.vcarrin87.jdbc_example.services.InventoryService;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    /*
     * This method is used to create a new inventory record.
     * Example of a POST request:
     * {
     *     "productId": 1,
     *     "quantity": 100
     * }
     */
    @PostMapping("/new-inventory")
    public ResponseEntity<String> createInventory(@RequestBody Inventory inventory) {
        try {
            inventoryService.createInventoryRecord(inventory);
            return ResponseEntity.ok("Inventory record created successfully");
        } catch (Exception e) {
            System.out.println("Error creating inventory record: " + e.getMessage());
            return ResponseEntity.status(500).body("Error creating inventory record: " + e.getMessage());
        }
    }

    /*
     * This method is used to get inventory by product ID.
     * Example of a GET request:
     * /inventory/product/1
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<Inventory> getInventoryByProductId(@PathVariable int productId) {
        Inventory inventory = inventoryService.getInventoryByProductId(productId);
        if (inventory != null) {
            return ResponseEntity.ok(inventory);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
