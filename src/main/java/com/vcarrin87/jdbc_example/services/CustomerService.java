package com.vcarrin87.jdbc_example.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vcarrin87.jdbc_example.models.Customer;
import com.vcarrin87.jdbc_example.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

     /**
     * This method creates a new customer.
     * @param customer The customer to create.
     */
    public void createCustomer(Customer customer) {
        customerRepository.save(customer);
        log.info("Customer created: {}", customer);
    }

    /**
     * Retrieves a customer by their ID.
     *
     * @param id the ID of the customer to retrieve
     * @return the customer with the specified ID, or null if not found
     */
    public Customer getCustomerById(int id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer != null) {
            log.info("Customer found: {}", customer);
        } else {
            log.warn("Customer with ID {} not found", id);
        }
        return customer;
    }

    /**
     * Retrieves the total number of customers in the database.
     *
     * @return the total number of customers
     */
    public List<Customer> getAllCustomers() {
        List<Customer> allCustomers = customerRepository.findAll();
        log.info("Customers: {}", allCustomers);
        return allCustomers;
    }

    /**
     * Updates an existing customer.
     *
     * @param customer the customer to update
     */
    public void updateCustomer(Customer customer) {
        customerRepository.save(customer);
        log.info("Customer updated: {}", customer);
    }

    /**
     * Deletes a customer by their ID. Cascades to their orders, order items, and payments
     * via the Customer.orders/Orders.orderItems/Orders.payments mappings.
     *
     * @param id the ID of the customer to delete
     */
    @Transactional
    public void deleteCustomer(int id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            log.info("Customer with ID {} and related orders/payments deleted", id);
        } else {
            log.warn("No customer found with ID {}", id);
        }
    }

    /**
     * Retrieves a customer along with their orders and payments.
     * The lazy collections are initialized here, while the transaction/session is still open.
     */
    @Transactional(readOnly = true)
    public List<Customer> getCustomerWithOrdersAndPayments(int customerId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            log.warn("Customer with ID {} not found", customerId);
            return List.of();
        }

        customer.getOrders().forEach(order -> {
            order.getOrderItems().size();
            order.getPayments().size();
        });

        List<Customer> customersWithOrdersAndPayments = List.of(customer);
        log.info("Customers with orders and payments: {}", customersWithOrdersAndPayments);
        return customersWithOrdersAndPayments;
    }
}
