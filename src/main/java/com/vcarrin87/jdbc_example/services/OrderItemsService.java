package com.vcarrin87.jdbc_example.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vcarrin87.jdbc_example.models.OrderItems;
import com.vcarrin87.jdbc_example.repository.OrderItemsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderItemsService {

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    /**
     * This method creates a new order item.
     * @param orderItem The order item to create.
     */
    public void createOrderItem(int orderId, int productId, int quantity, double price) {
        int orderItemId = orderItemsRepository.save(orderId, productId, quantity, price);
        log.info("Order item created: {}", orderItemId);
    }

    /**
     * Get all order items.
     */
    public List<OrderItems> getAllOrderItems() {
        List<OrderItems> allOrderItems = orderItemsRepository.findAll();
        log.info("Retrieved all order items: {}", allOrderItems);
        return allOrderItems;
    }

    /**
     * Get order items by order ID.
     */ 
    public List<OrderItems> getOrderItemsByOrderId(int orderId) {
        List<OrderItems> orderItems = orderItemsRepository.findByOrderId(orderId);
        if (orderItems != null && !orderItems.isEmpty()) {
            log.info("Order items found for order ID {}: {}", orderId, orderItems);
        } else {
            log.warn("No order items found for order ID {}", orderId);
        }
        return orderItems;
    }
}
