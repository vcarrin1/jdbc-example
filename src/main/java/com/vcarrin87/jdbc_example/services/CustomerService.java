package com.vcarrin87.jdbc_example.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vcarrin87.jdbc_example.models.Customer;
import com.vcarrin87.jdbc_example.repository.CustomerRepository;
import com.vcarrin87.jdbc_example.repository.OrdersRepository;
import com.vcarrin87.jdbc_example.repository.PaymentsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private PaymentsRepository paymentsRepository;

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
    public Customer getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id);
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
        int rowsAffected = customerRepository.update(customer);
        if (rowsAffected > 0) {
            log.info("Customer updated: {}", customer);
        } else {
            log.warn("No customer found with ID {}", customer.getCustomerId());
        }   
    }

    /**
     * Deletes a customer by their ID.
     *
     * @param id the ID of the customer to delete
     */
    @Transactional
    public void deleteCustomer(Long id) {
        paymentsRepository.deletePaymentsByCustomerId(id);
        ordersRepository.deleteOrdersByCustomerId(id);
        int rowsAffected = customerRepository.deleteById(id);
        if (rowsAffected > 0) {
            log.info("Customer with ID {} and related orders/payments deleted", id);
        } else {
            log.warn("No customer found with ID {}", id);
        }
    }

    /**
     * Retrieves customers along with their orders and payments.
     */
    public List<Customer> getCustomerWithOrdersAndPayments(int customerId) {
        List<Customer> customersWithOrdersAndPayments = customerRepository.getCustomerWithOrdersAndPayments(customerId);
        log.info("Customers with orders and payments: {}", customersWithOrdersAndPayments);
        return customersWithOrdersAndPayments;  
    }
}
