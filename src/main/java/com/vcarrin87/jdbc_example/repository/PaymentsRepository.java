package com.vcarrin87.jdbc_example.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.vcarrin87.jdbc_example.models.Payments;

@Repository
public class PaymentsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Payments> paymentsRowMapper = new RowMapper<Payments>() {
        @Override
        public Payments mapRow(ResultSet rs, int rowNum) throws SQLException {
            Payments payment = new Payments();
            payment.setPaymentId(rs.getInt("payment_id"));
            payment.setOrderId(rs.getInt("order_id"));
            payment.setAmount(rs.getDouble("amount"));
            payment.setPaymentDate(rs.getTimestamp("payment_date"));
            payment.setPaymentMethod(rs.getString("payment_method"));
            return payment;
        }
    };

    public List<Payments> findAll() {
        String sql = "SELECT * FROM payments";
        return jdbcTemplate.query(sql, paymentsRowMapper);
    }

    public Payments findById(int paymentId) {
        String sql = "SELECT * FROM payments WHERE payment_id = ?";
        return jdbcTemplate.queryForObject(sql, paymentsRowMapper, paymentId);
    }

    public int save(int orderId, double amount, Timestamp paymententDate, String paymentMethod) {
        String sql = "INSERT INTO payments (order_id, amount, payment_date, payment_method) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, orderId, amount, paymententDate, paymentMethod);
    }

    public int update(Payments payment) {
        String sql = "UPDATE payments SET order_id = ?, amount = ?, payment_date = ?, payment_method = ? WHERE payment_id = ?";
        return jdbcTemplate.update(sql, payment.getOrderId(), payment.getAmount(), payment.getPaymentDate(), payment.getPaymentMethod(), payment.getPaymentId());
    }

    public int deleteById(int paymentId) {
        String sql = "DELETE FROM payments WHERE payment_id = ?";
        return jdbcTemplate.update(sql, paymentId);
    }

    public int deletePaymentsByOrderId(int orderId) {
        String sql = "DELETE FROM payments WHERE order_id = ?";
        return jdbcTemplate.update(sql, orderId);
    }

    public int deletePaymentsByCustomerId(Long customerId) {
        String sql = "DELETE FROM payments WHERE order_id IN (SELECT order_id FROM orders WHERE customer_id = ?)";
        return jdbcTemplate.update(sql, customerId);
    }
}
