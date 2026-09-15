package com.vcarrin87.jdbc_example.services;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vcarrin87.jdbc_example.models.OrderItems;
import com.vcarrin87.jdbc_example.models.Orders;
import com.vcarrin87.jdbc_example.models.Payments;
import com.vcarrin87.jdbc_example.repository.CustomerRepository;
import com.vcarrin87.jdbc_example.repository.InventoryRepository;
import com.vcarrin87.jdbc_example.repository.OrderItemsRepository;
import com.vcarrin87.jdbc_example.repository.OrdersRepository;
import com.vcarrin87.jdbc_example.repository.PaymentsRepository;
import com.vcarrin87.jdbc_example.repository.ProductsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Autowired
    private PaymentsRepository paymentsRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    /**
     * This method creates a new order.
     * @param order The order to create. Its customer only needs customerId set;
     *              it is resolved to a managed reference before saving.
     */
    public int createOrder(Orders order) {
        order.setCustomer(customerRepository.getReferenceById(order.getCustomer().getCustomerId()));
        int newOrderId = ordersRepository.save(order).getOrderId();
        log.info("Order created: {}", order);
        return newOrderId;
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param id the ID of the order to retrieve
     * @return the order with the specified ID, or null if not found
     */
    public Orders getOrderById(int id) {
        Orders order = ordersRepository.findById(id).orElse(null);
        if (order != null) {
            log.info("Order found: {}", order);
        } else {
            log.warn("Order with ID {} not found", id);
        }
        return order;
    }

    /**
     * Retrieves all orders in the database. Lazy order items/payments are initialized here
     * since they are serialized to the client (see Orders.orderItems/payments mappings).
     *
     * @return list of orders
     */
    @Transactional(readOnly = true)
    public List<Orders> getAllOrders() {
        List<Orders> allOrders = ordersRepository.findAll();
        allOrders.forEach(order -> {
            order.getOrderItems().size();
            order.getPayments().size();
        });
        log.info("Retrieved all orders: {}", allOrders);
        return allOrders;
    }

    /**
     * Deletes an order by its ID. Cascades to its order items and payments
     * via the Orders.orderItems/Orders.payments mappings.
     *
     * @param id the ID of the order to delete
     */
    @Transactional
    public void deleteOrder(int id) {
        if (ordersRepository.existsById(id)) {
            ordersRepository.deleteById(id);
            log.info("Order with ID {} deleted", id);
        } else {
            log.warn("Order with ID {} not found for deletion", id);
        }
    }

    /**
     * Updates an existing order.
     */
    public void updateOrder(Orders order) {
        order.setCustomer(customerRepository.getReferenceById(order.getCustomer().getCustomerId()));
        ordersRepository.save(order);
        log.info("Order updated: {}", order);
    }

    @Transactional
    public void placeOrder(int customerId, String orderStatus, Date deliveryDate, List<OrderItems> orderItems) {
        Orders order = new Orders();
        order.setCustomer(customerRepository.getReferenceById(customerId));
        order.setOrderStatus(orderStatus);
        order.setDeliveryDate(deliveryDate);
        log.info("Placing order: {}", order);
        // Insert order and retrieve generated ID
        int newOrderId = ordersRepository.saveWithGeneratedKey(order);
        log.info("New order created with ID: {}", newOrderId);

        // Extract generated order ID
        order.setOrderId(newOrderId);  // Set ID on order object

        double totalAmount = 0;

        // Save each order item after the order is created
        for (OrderItems item : orderItems) {

            log.info("Processing order item: {}", item);
            int productId = item.getProduct().getProductId();
            int quantity = item.getQuantity();
            Double price = productsRepository.getProductPriceById(productId);
            log.info("Product ID: {}, Quantity: {}, Price: {}", productId, quantity, price);

            // Add order item with the correct order/product references
            OrderItems orderItem = new OrderItems();
            orderItem.setOrder(ordersRepository.getReferenceById(newOrderId));
            orderItem.setProduct(productsRepository.getReferenceById(productId));
            orderItem.setQuantity(quantity);
            orderItem.setPrice(price * quantity);
            orderItemsRepository.save(orderItem);
            totalAmount += price * quantity;
            log.info("Added order item: productId={}, quantity={}, price={}, totalAmount={}", productId, quantity, price, totalAmount);
            // Update inventory
            inventoryRepository.updateInventory(productId, -quantity);
            log.info("Updated inventory for product ID: {}", productId);
        }

        // Add payment
        Timestamp paymentDate = new Timestamp(System.currentTimeMillis());
        log.info("Payment date: {}", paymentDate);
        Payments payment = new Payments();
        payment.setOrder(ordersRepository.getReferenceById(newOrderId));
        payment.setAmount(totalAmount);
        payment.setPaymentDate(paymentDate);
        payment.setPaymentMethod("CREDIT_CARD");
        paymentsRepository.save(payment);

        log.info("Order placed successfully: {}", order);
    }
}

