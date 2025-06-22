package com.vcarrin87.jdbc_example.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.vcarrin87.jdbc_example.models.OrderItems;
import com.vcarrin87.jdbc_example.models.Orders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

@Repository
public class OrdersRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Orders> ordersRowMapper = new RowMapper<Orders>() {
        @Override
        public Orders mapRow(ResultSet rs, int rowNum) throws SQLException {
            Orders order = new Orders();
            order.setOrderId(rs.getInt("order_id"));
            order.setCustomerId(rs.getInt("customer_id"));
            order.setOrderStatus(rs.getString("order_status"));
            order.setDeliveryDate(rs.getDate("delivery_date"));
            return order;
        }
    };

    public List<Orders> findAll() {
        String sql = "SELECT * FROM orders";
        return jdbcTemplate.query(sql, ordersRowMapper);
    }

    public Orders findById(int orderId) {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        return jdbcTemplate.queryForObject(sql, ordersRowMapper, orderId);
    }

    public int save(Orders order) {
        String sql = "INSERT INTO orders (customer_id, order_status, delivery_date) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, order.getCustomerId(), order.getOrderStatus(), order.getDeliveryDate());
    }

    // This method saves an order and returns the generated key (order_id)
    // this generated key can be used to insert related order items in the same transaction
    public int saveWithGeneratedKey(Orders order) {
        String sql = "INSERT INTO orders (customer_id, order_status, delivery_date) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, order.getCustomerId());
            ps.setString(2, order.getOrderStatus());
            ps.setDate(3, new java.sql.Date(order.getDeliveryDate().getTime()));
            return ps;
        }, keyHolder);
        Number key = (Number) keyHolder.getKeys().get("order_id");
        return key != null ? key.intValue() : -1;
    }

    public int update(Orders order) {
        String sql = "UPDATE orders SET customer_id = ?, order_status = ?, delivery_date = ? WHERE order_id = ?";
        return jdbcTemplate.update(sql, order.getCustomerId(), order.getOrderStatus(), order.getDeliveryDate(), order.getOrderId());
    }

    // get order_items by order ID
    public List<OrderItems> getOrderItemsByOrderId(int orderId) {
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        return jdbcTemplate.query(sql, new RowMapper<OrderItems>() {
            @Override
            public OrderItems mapRow(ResultSet rs, int rowNum) throws SQLException {
                OrderItems orderItem = new OrderItems();
                orderItem.setOrderId(rs.getInt("order_id"));
                orderItem.setOrderItemId(rs.getInt("orderitem_id"));
                orderItem.setProductId(rs.getInt("product_id"));
                orderItem.setQuantity(rs.getInt("quantity"));
                orderItem.setPrice(rs.getDouble("price"));
                return orderItem;
            }
        }, orderId);
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
