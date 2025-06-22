package com.vcarrin87.jdbc_example.rest;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.vcarrin87.jdbc_example.models.OrderItems;
import com.vcarrin87.jdbc_example.models.Orders;
import com.vcarrin87.jdbc_example.repository.ProductsRepository;
import com.vcarrin87.jdbc_example.services.OrdersService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private ProductsRepository productsRepository;

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

    /**
     * Place an order
     * Example of a POST request:
     * /orders/place-order?customer_id=1&order_status=PENDING&delivery_date=2025-06-22
     */
    @PostMapping("/place-order")
    public ResponseEntity<String> placeOrder(
            @RequestParam("customer_id") int customerId,
            @Parameter(
                description = "Order status. Allowed values: PENDING, PROCESSING, COMPLETED.",
                schema = @Schema(
                    type = "string",
                    allowableValues = {"PENDING", "PROCESSING", "COMPLETED"}
                )
            )
            @RequestParam("order_status") String orderStatus,
            @Parameter(
                description = "The delivery_date parameter defaults to the current date and time in ISO format if not provided.",
                example = "2025-06-22"
            )
            @RequestParam(value = "delivery_date", required = false) String deliveryDate,
            @Parameter(
                description = "Comma-separated list of product IDs to add to the order.",
                example = "1,2,3"
            )
            @RequestParam(value = "product_ids", required = true) String productIds
    ) {
        try {
            Date dateToUse;
            if (deliveryDate == null || deliveryDate.isEmpty()) {
                dateToUse = new Date(System.currentTimeMillis());
            } else {
                try {
                    dateToUse = Date.valueOf(deliveryDate.substring(0, 10));
                } catch (Exception parseEx) {
                    return ResponseEntity.badRequest().body("Invalid delivery_date format. Expected format: yyyy-MM-dd or ISO 8601 date string.");
                }
            }

            List<OrderItems> orderItemsList = new ArrayList<>();
            String[] productIdArray = productIds.split(",");
            for (String productIdStr : productIdArray) {
                int productId;
                try {
                    productId = Integer.parseInt(productIdStr.trim());
                } catch (NumberFormatException nfe) {
                    return ResponseEntity.badRequest().body("Invalid product ID: " + productIdStr);
                }
                // Fetch product details from DB using a service method (assume getProductById exists)
                var product = productsRepository.findById(productId);
                if (product == null) {
                    return ResponseEntity.badRequest().body("Product not found for ID: " + productId);
                }
                OrderItems orderItem = new OrderItems();
                // Set orderId later after order is created if needed
                orderItem.setProductId(productId);
                orderItem.setQuantity(1); // Default quantity, or you can extend to accept quantities
                orderItem.setPrice(product.getPrice());
                orderItemsList.add(orderItem);
            }

            ordersService.placeOrder(customerId, orderStatus, dateToUse, orderItemsList);
            return ResponseEntity.ok("Order placed successfully");
        } catch (Exception e) {
            System.out.println("Error placing order: " + e.getMessage());
            return ResponseEntity.status(500).body("Error placing order: " + e.getMessage());
        }
    }
}
