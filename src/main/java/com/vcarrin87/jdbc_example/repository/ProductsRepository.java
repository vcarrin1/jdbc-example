package com.vcarrin87.jdbc_example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vcarrin87.jdbc_example.models.Products;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Integer> {

    // Get products in stock
    @Query("SELECT p FROM Products p JOIN Inventory i ON i.productId = p.productId WHERE i.stockLevel > 0")
    List<Products> getProductsInStock();

    // Get product price by ID
    @Query("SELECT p.price FROM Products p WHERE p.productId = :productId")
    Double getProductPriceById(@Param("productId") int productId);
}
