package com.vcarrin87.jdbc_example.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vcarrin87.jdbc_example.models.Payments;
import com.vcarrin87.jdbc_example.repository.OrdersRepository;
import com.vcarrin87.jdbc_example.repository.PaymentsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentsService {

    @Autowired
    private PaymentsRepository paymentsRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    /**
     * This method creates a new payment.
     * @param payment The payment to create. Its order only needs orderId set;
     *                it is resolved to a managed reference before saving.
     */
    public void createPayment(Payments payment) {
        payment.setOrder(ordersRepository.getReferenceById(payment.getOrder().getOrderId()));
        paymentsRepository.save(payment);
        log.info("Payment created: {}", payment);
    }

    /**
     * Retrieves all payments in the database.
     */
    public List<Payments> getAllPayments() {
        List<Payments> allPayments = paymentsRepository.findAll();
        log.info("Retrieved all payments: {}", allPayments);
        return allPayments;
    }

    /**
     * Retrieves a payment by its ID.
     *
     * @param id the ID of the payment to retrieve
     * @return the payment with the specified ID, or null if not found
     */
    public Payments getPaymentById(int id) {
        Payments payment = paymentsRepository.findById(id).orElse(null);
        if (payment != null) {
            log.info("Payment found: {}", payment);
        } else {
            log.warn("Payment with ID {} not found", id);
        }
        return payment;
    }

    /**
     * Updates an existing payment.
     * @param id
     */
    public void updatePayment(Payments payment) {
        payment.setOrder(ordersRepository.getReferenceById(payment.getOrder().getOrderId()));
        paymentsRepository.save(payment);
        log.info("Payment updated: {}", payment);
    }

    /**
     * Deletes a payment by its ID.
     *
     * @param id the ID of the payment to delete
     */
    public void deletePayment(int id) {
        if (paymentsRepository.existsById(id)) {
            paymentsRepository.deleteById(id);
            log.info("Payment with ID {} deleted", id);
        } else {
            log.warn("Payment with ID {} not found for deletion", id);
        }
    }
}
