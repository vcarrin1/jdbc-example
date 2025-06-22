package com.vcarrin87.jdbc_example.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vcarrin87.jdbc_example.services.OrderItemsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;
import com.vcarrin87.jdbc_example.models.OrderItems;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/order-items")
public class OrderItemsController {

    @Autowired
    private OrderItemsService orderItemsService;
    
    /**
     * Creates a new order item.
     * Example POST request:
     * /order-items/new-order-item?orderId=1&productId=2&quantity=3&price=10.5
     */
    @PostMapping("/new-order-item")
    public ResponseEntity<String> createOrderItem(
            @RequestParam("orderId") int orderId,
            @RequestParam("productId") int productId,
            @RequestParam("quantity") int quantity,
            @RequestParam("price") double price) {
        try {
            orderItemsService.createOrderItem(orderId, productId, quantity, price);
            return ResponseEntity.ok("Order item created successfully");
        } catch (Exception e) {
            System.out.println("Error creating order item: " + e.getMessage());
            return ResponseEntity.status(500).body("Error creating order item: " + e.getMessage());
        }
    }

    /**
     * This method is used to get all order items.
     * Example of a GET request:
     * /order-items/all-order-items
     */
    @RequestMapping("/all-order-items")
    public ResponseEntity<List<OrderItems>> getAllOrderItems() {
        List<OrderItems> orderItems = orderItemsService.getAllOrderItems();
        return ResponseEntity.ok(orderItems);
    }

    /**
     * Gets an order item by order ID.
     */
    @RequestMapping("/order-item-by-order-id/{orderId}")
    public ResponseEntity<List<OrderItems>> getOrderItemsByOrderId(@RequestParam("orderId") int orderId) {
        List<OrderItems> orderItems = orderItemsService.getOrderItemsByOrderId(orderId);
        if (orderItems != null && !orderItems.isEmpty()) {
            return ResponseEntity.ok(orderItems);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
