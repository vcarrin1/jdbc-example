package com.vcarrin87.jdbc_example.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vcarrin87.jdbc_example.models.Orders;
import com.vcarrin87.jdbc_example.repository.OrderItemsRepository;
import com.vcarrin87.jdbc_example.repository.OrdersRepository;
import com.vcarrin87.jdbc_example.repository.PaymentsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Autowired
    private PaymentsRepository paymentsRepository;

    /**
     * This method creates a new order.
     * @param order The order to create.
     */
    public void createOrder(Orders order) {
        ordersRepository.save(order);
        log.info("Order created: {}", order);
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param id the ID of the order to retrieve
     * @return the order with the specified ID, or null if not found
     */
    public Orders getOrderById(Long id) {
        Orders order = ordersRepository.findById(id);
        if (order != null) {
            log.info("Order found: {}", order);
        } else {
            log.warn("Order with ID {} not found", id);
        }
        return order;
    }

    /**
     * Retrieves all orders in the database.
     *
     * @return list of orders
     */
    public List<Orders> getAllOrders() {
        List<Orders> allOrders = ordersRepository.findAll();
        log.info("Retrieved all orders: {}", allOrders);
        return allOrders;
    }

    /**
     * Deletes an order by its ID.
     *
     * @param id the ID of the order to delete
     */
    @Transactional
    public void deleteOrder(int id) {
        orderItemsRepository.deleteByOrderId(id);
        paymentsRepository.deletePaymentsByOrderId(id);
        int rowsAffected = ordersRepository.deleteById(id);
        if (rowsAffected > 0) {
            log.info("Order with ID {} deleted", id);
        } else {
            log.warn("Order with ID {} not found for deletion", id);
        }
    }

    /**
     * Updates an existing order.
     */
    public void updateOrder(Orders order) {
        int rowsAffected = ordersRepository.update(order);
        if (rowsAffected > 0) {
            log.info("Order updated: {}", order);
        } else {
            log.warn("No order found with ID {}", order.getOrderId());
        }
    }
}
