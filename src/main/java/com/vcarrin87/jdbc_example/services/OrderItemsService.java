package com.vcarrin87.jdbc_example.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vcarrin87.jdbc_example.models.OrderItems;
import com.vcarrin87.jdbc_example.repository.OrderItemsRepository;
import com.vcarrin87.jdbc_example.repository.OrdersRepository;
import com.vcarrin87.jdbc_example.repository.ProductsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderItemsService {

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private ProductsRepository productsRepository;

    /**
     * This method creates a new order item, resolving the order/product ids to managed references.
     */
    public void createOrderItem(int orderId, int productId, int quantity, double price) {
        OrderItems orderItem = new OrderItems();
        orderItem.setOrder(ordersRepository.getReferenceById(orderId));
        orderItem.setProduct(productsRepository.getReferenceById(productId));
        orderItem.setQuantity(quantity);
        orderItem.setPrice(price);
        orderItem = orderItemsRepository.save(orderItem);
        log.info("Order item created: {}", orderItem.getOrderItemId());
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
     * Get order items by order ID, navigating the Orders.orderItems relationship.
     */ 
    public List<OrderItems> getOrderItemsByOrderId(int orderId) {
        List<OrderItems> orderItems = ordersRepository.findById(orderId)
                .map(order -> order.getOrderItems())
                .orElse(List.of());
        if (!orderItems.isEmpty()) {
            log.info("Order items found for order ID {}: {}", orderId, orderItems);
        } else {
            log.warn("No order items found for order ID {}", orderId);
        }
        return orderItems;
    }
}
