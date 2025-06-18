package com.vcarrin87.jdbc_example.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.vcarrin87.jdbc_example.models.OrderItems;

@Repository
public class OrderItemsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<OrderItems> orderItemsRowMapper = new RowMapper<OrderItems>() {
        @Override
        public OrderItems mapRow(ResultSet rs, int rowNum) throws SQLException {
            OrderItems orderItem = new OrderItems();
            orderItem.setOrderId(rs.getInt("order_id"));
            orderItem.setOrderItemId(rs.getInt("orderitem_id"));
            orderItem.setQuantity(rs.getInt("quantity"));
            orderItem.setPrice(rs.getDouble("price"));
            return orderItem;
        }
    };

    /**
     * Create a new order item.
     */
    public int save(OrderItems orderItem) {
        String sql = "INSERT INTO order_items (order_id, quantity, price) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, orderItem.getOrderId(), orderItem.getQuantity(), orderItem.getPrice());
    }

    /**
     * Delete an order item by order ID.
     */
    public int deleteByOrderId(int orderId) {
        String sql = "DELETE FROM order_items WHERE order_id = ?";
        return jdbcTemplate.update(sql, orderId);
    }

    /**
     * Delete an order item by product ID.
     */
    public int deleteOrderItemByProductId(int productId) {
        String sql = "DELETE FROM order_items WHERE product_id = ?";
        return jdbcTemplate.update(sql, productId);
    }
}
