package com.vcarrin87.jdbc_example.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import com.vcarrin87.jdbc_example.models.Orders;
import com.vcarrin87.jdbc_example.services.OrdersService;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    /*
     * This method is used to create a new order.
     * Example of a POST request:
    {
        "customerId": 1,
        "orderStatus": "PENDING",
        "deliveryDate": "2025-05-20T03:44:55.123Z"
    }
     */
    @PostMapping("/new-order")
    public ResponseEntity<String> createOrder(@RequestBody Orders order) {
        try {
            ordersService.createOrder(order);
            return ResponseEntity.ok("Order created successfully");
        } catch (Exception e) {
            System.out.println("Error creating order: " + e.getMessage());
            return ResponseEntity.status(500).body("Error creating order: " + e.getMessage());
        }
    }

    /*
     * This method is used to get all orders.
     * Example of a GET request:
     /orders/all-orders
     */
    @GetMapping("/all-orders")
    public List<Orders> getAllOrders() {
        return ordersService.getAllOrders();
    }

    /*
     * This method is used to update an order.
     * Example of a GET request:
     /orders/1
     */
    @PostMapping("/update-order")
    public ResponseEntity<String> updateOrder(@RequestBody Orders order) {
        try {
            ordersService.updateOrder(order);
            return ResponseEntity.ok("Order updated successfully");
        } catch (Exception e) {
            System.out.println("Error updating order: " + e.getMessage());
            return ResponseEntity.status(500).body("Error updating order: " + e.getMessage());
        }
    }

    /*
     * This method is used to delete an order by its ID.
     * Example of a DELETE request:
     /orders/delete-order/1
     */
    @DeleteMapping("/delete-order/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable int orderId) {
        try {
            ordersService.deleteOrder(orderId);
            return ResponseEntity.ok("Order deleted successfully");
        } catch (Exception e) {
            System.out.println("Error deleting order: " + e.getMessage());
            return ResponseEntity.status(500).body("Error deleting order: " + e.getMessage());
        }
    }
}
