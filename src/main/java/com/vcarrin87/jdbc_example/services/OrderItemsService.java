package com.vcarrin87.jdbc_example.services;

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
    public void createOrderItem(OrderItems orderItem) {
        orderItemsRepository.save(orderItem);
        log.info("Order item created: {}", orderItem);
    }
}
