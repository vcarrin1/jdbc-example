package com.vcarrin87.jdbc_example.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vcarrin87.jdbc_example.models.Payments;
import com.vcarrin87.jdbc_example.services.PaymentsService;

@RestController
@RequestMapping("/payments")
public class PaymentsController {

    @Autowired
    private PaymentsService paymentsService;

    /*
     * This method is used to get all payments.
     * Example of a GET request:
     /payments/all-payments
     */
    @GetMapping("/all-payments")
    public List<Payments> getAllPayments() {
        return paymentsService.getAllPayments();
    }

    /*
     * This method is used to create a new payment.
     * Example of a POST request:
     {
         "orderId": 1,
         "paymentDate": "2025-05-20T03:44:55.123Z",
         "amount": 100.00,
         "paymentMethod": "CREDIT_CARD"
     }
     */
    @PostMapping("/new-payment")        
    public ResponseEntity<String> createPayment(@RequestBody Payments payment) {
        try {
            paymentsService.createPayment(payment);
            return ResponseEntity.ok("Payment created successfully");
        } catch (Exception e) {
            System.out.println("Error creating payment: " + e.getMessage());
            return ResponseEntity.status(500).body("Error creating payment: " + e.getMessage());
        }
    }

    /*
     * This method is used to get a payment by its ID.
     * Example of a GET request:
     /payments/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<Payments> getPaymentById(@RequestParam int id) {
        Payments payment = paymentsService.getPaymentById(id);
        if (payment != null) {
             return ResponseEntity.ok(payment);
        }
        return ResponseEntity.notFound().build();
    }
    /*
     * This method is used to update a payment.
     * Example of a POST request:
     {
         "paymentId": 1,
         "orderId": 1,
         "paymentDate": "2025-05-20T03:44:55.123Z",
         "amount": 100.00,
         "paymentMethod": "CREDIT_CARD"
     }
     */
    @PostMapping("/update-payment")
    public ResponseEntity<String> updatePayment(@RequestBody Payments payment) {
        try {
            paymentsService.updatePayment(payment);
            return ResponseEntity.ok("Payment updated successfully");
        } catch (Exception e) {
            System.out.println("Error updating payment: " + e.getMessage());
            return ResponseEntity.status(500).body("Error updating payment: " + e.getMessage());
        }
    }
        /*
     * This method is used to delete a payment by its ID.
     * Example of a DELETE request:
     /payments/1
     */
    @DeleteMapping("/delete-payment/{id}")
    public ResponseEntity<String> deletePayment(@PathVariable int id) {
        try {
            paymentsService.deletePayment(id);
            return ResponseEntity.ok("Payment deleted successfully");
        } catch (Exception e) {
            System.out.println("Error deleting payment: " + e.getMessage());
            return ResponseEntity.status(500).body("Error deleting payment: " + e.getMessage());
        }
    }
}
