package com.vcarrin87.jdbc_example.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.vcarrin87.jdbc_example.models.Orders;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

@Repository
public class OrdersRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Orders> ordersRowMapper = new RowMapper<Orders>() {
        @Override
        public Orders mapRow(ResultSet rs, int rowNum) throws SQLException {
            Orders order = new Orders();
            order.setCustomerId(rs.getInt("customer_id"));
            order.setOrderStatus(rs.getString("amount"));
            order.setDeliveryDate(rs.getDate("delivery_date"));
            return order;
        }
    };

    public List<Orders> findAll() {
        String sql = "SELECT * FROM orders";
        return jdbcTemplate.query(sql, ordersRowMapper);
    }

    public Orders findById(Long orderId) {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        return jdbcTemplate.queryForObject(sql, ordersRowMapper, orderId);
    }

    public int save(Orders order) {
        String sql = "INSERT INTO orders (customer_id, order_status, delivery_date) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, order.getCustomerId(), order.getOrderStatus(), order.getDeliveryDate());
    }

    public int update(Orders order) {
        String sql = "UPDATE orders SET customer_id = ?, order_status = ?, delivery_date = ? WHERE order_id = ?";
        return jdbcTemplate.update(sql, order.getCustomerId(), order.getOrderStatus(), order.getDeliveryDate(), order.getOrderId());
    }

    public int deleteById(int orderId) {
        String sql = "DELETE FROM orders WHERE order_id = ?";
        return jdbcTemplate.update(sql, orderId);
    }

    public int deleteOrdersByCustomerId(Long id) {
        String sql = "DELETE FROM orders WHERE customer_id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
