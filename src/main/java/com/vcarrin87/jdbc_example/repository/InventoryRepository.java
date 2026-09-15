package com.vcarrin87.jdbc_example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vcarrin87.jdbc_example.models.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    /**
     * Increments (or decrements, for a negative delta) the stock level for a product.
     */
    @Modifying
    @Query("UPDATE Inventory i SET i.stockLevel = i.stockLevel + :delta WHERE i.productId = :productId")
    void updateInventory(@Param("productId") int productId, @Param("delta") int delta);

    Inventory findByProductId(int productId);

    void deleteByProductId(int productId);
}
