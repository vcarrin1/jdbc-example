package com.vcarrin87.jdbc_example.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vcarrin87.jdbc_example.services.OrderItemsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import com.vcarrin87.jdbc_example.models.OrderItems;

@RestController
@RequestMapping("/order-items")
public class OrderItemsController {

    @Autowired
    private OrderItemsService orderItemsService;

    @PostMapping("/new-order-item")
    /**
     * This method is used to create a new order item.
     * Example of a POST request:
     * {
     *     "orderId": 1,
     *     "productId": 2,
     *     "quantity": 3,
     *     "price": 19.99
     * }
     */
    public ResponseEntity<String> createOrderItem(@RequestBody OrderItems orderItem) {
        try {
            orderItemsService.createOrderItem(orderItem);
            return ResponseEntity.ok("Order item created successfully");
        } catch (Exception e) {
            System.out.println("Error creating order item: " + e.getMessage());
            return ResponseEntity.status(500).body("Error creating order item: " + e.getMessage());
        }
    }
}
