package com.vcarrin87.jdbc_example.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.vcarrin87.jdbc_example.models.Inventory;

@Repository
public class InventoryRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Inventory> inventoryRowMapper = new RowMapper<Inventory>() {
        @Override
        public Inventory mapRow(ResultSet rs, int rowNum) throws SQLException {
            Inventory inventory = new Inventory();
            inventory.setProductId(rs.getInt("product_id"));
            inventory.setStockLevel(rs.getInt("stock_level"));
            return inventory;
        }
    };

    /**
     * Create a new inventory record.
     */
    public int save(Inventory inventory) {
        String sql = "INSERT INTO inventory (product_id, stock_level) VALUES (?, ?)";
        return jdbcTemplate.update(sql, inventory.getProductId(), inventory.getStockLevel());
    }

    /**
     * Find an inventory record by product ID.
     * @param productId
     * @param stockLevel
     * @return
     */
    public Inventory findByProductId(int productId) {
        String sql = "SELECT * FROM inventory WHERE product_id = ?";
        return jdbcTemplate.queryForObject(sql, inventoryRowMapper, productId);
    }

    /**
     * Update the stock level for a product.
     */
    public int updateStockLevel(int productId, int stockLevel) {
        String sql = "UPDATE inventory SET stock_level = ? WHERE product_id = ?";
        return jdbcTemplate.update(sql, stockLevel, productId);
    }

    /**
     * Delete an inventory record by product ID.
     */
    public int deleteInventoryByProductId(int productId) {
        String sql = "DELETE FROM inventory WHERE product_id = ?";
        return jdbcTemplate.update(sql, productId);
    }
}
