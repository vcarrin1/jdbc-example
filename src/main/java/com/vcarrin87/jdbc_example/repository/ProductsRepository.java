package com.vcarrin87.jdbc_example.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.vcarrin87.jdbc_example.models.OrderItems;
import com.vcarrin87.jdbc_example.models.Products;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class ProductsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Products> productsRowMapper = new RowMapper<Products>() {
        @Override
        public Products mapRow(ResultSet rs, int rowNum) throws SQLException {
            Products product = new Products();
            product.setProductId(rs.getInt("product_id"));
            product.setName(rs.getString("name"));
            product.setDescription(rs.getString("description"));
            product.setPrice(rs.getDouble("price"));
            return product;
        }
    };

    // Method to find all products
    public List<Products> findAll() {
        String sql = "SELECT * FROM products";
        return jdbcTemplate.query(sql, productsRowMapper);
    }

    // Method to find a product by its ID
    public Products findById(int productId) {
        String sql = "SELECT * FROM products WHERE product_id = ?";
        return jdbcTemplate.queryForObject(sql, productsRowMapper, productId);
    }

    // Method to save a new product
    public int save(Products product) {
        String sql = "INSERT INTO products (name, description, price) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, product.getName(), product.getDescription(), product.getPrice());
    }

    // Method to update an existing product
    public int update(Products product) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ? WHERE product_id = ?";
        return jdbcTemplate.update(sql, product.getName(), product.getDescription(), product.getPrice(), product.getProductId());
    }

    // Method to delete a product by its ID
    public int deleteById(int productId) {
        String sql = "DELETE FROM products WHERE product_id = ?";
        return jdbcTemplate.update(sql, productId);
    }

    // Get product with order items
    public Products getProductWithOrderItems(int productId) {
        String sql = "SELECT * FROM products p LEFT JOIN order_items oi ON p.product_id = oi.product_id WHERE p.product_id = ?";
        return jdbcTemplate.query(sql, rs -> {
            Products product = null;
            List<OrderItems> orderItems = new ArrayList<>();
            while (rs.next()) {
                if (product == null) {
                    product = new Products();
                    product.setProductId(rs.getInt("product_id"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getDouble("price"));
                }
                OrderItems item = new OrderItems();
                item.setOrderItemId(rs.getInt("orderitem_id"));
                item.setOrderId(rs.getInt("order_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setPrice(rs.getDouble("price"));
                orderItems.add(item);
            }
            if (product != null) {
                product.setOrderItems(orderItems);
            }
            return product;
        }, productId);
    }

    // Get products in stock
    public List<Products> getProductsInStock() {
        String sql = "SELECT * FROM products p INNER JOIN inventory i ON p.product_id = i.product_id WHERE i.stock_level > 0";
        return jdbcTemplate.query(sql, productsRowMapper);
    }

}
