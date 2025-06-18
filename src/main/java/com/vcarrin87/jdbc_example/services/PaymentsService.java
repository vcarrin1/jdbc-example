package com.vcarrin87.jdbc_example.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vcarrin87.jdbc_example.models.Payments;
import com.vcarrin87.jdbc_example.repository.PaymentsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentsService {

    @Autowired
    private PaymentsRepository paymentsRepository;

    /**
     * This method creates a new payment.
     * @param paymentId The ID of the payment to create.
     */
    public void createPayment(Payments payment) {
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
        Payments payment = paymentsRepository.findById(id);
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
        paymentsRepository.save(payment);
        log.info("Payment updated: {}", payment);
    }

    /**
     * Deletes a payment by its ID.
     *
     * @param id the ID of the payment to delete
     */
    public void deletePayment(int id) {
        int rowsAffected = paymentsRepository.deleteById(id);
        if (rowsAffected > 0) {
            log.info("Payment with ID {} deleted", id);
        } else {
            log.warn("Payment with ID {} not found for deletion", id);
        }
    }
}
