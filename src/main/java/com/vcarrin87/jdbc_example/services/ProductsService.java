package com.vcarrin87.jdbc_example.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vcarrin87.jdbc_example.models.Products;
import com.vcarrin87.jdbc_example.repository.InventoryRepository;
import com.vcarrin87.jdbc_example.repository.OrderItemsRepository;
import com.vcarrin87.jdbc_example.repository.ProductsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductsService {

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    /**
     * This method creates a new product.
     * @param product The product to create.
     */
    public void createProduct(Products product) {
        productsRepository.save(product);
        log.info("Product created: {}", product);   
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id the ID of the product to retrieve
     * @return the product with the specified ID, or null if not found
     */
    public Products getProductById(int id) {
        Products product = productsRepository.findById(id);
        if (product != null) {
            log.info("Product found: {}", product);
        } else {
            log.warn("Product with ID {} not found", id);
        }
        return product;
    }

    /**
     * Update an existing product.
     * @return
     */
    public void updateProduct(Products product) {
        productsRepository.save(product);
        log.info("Product updated: {}", product);
    }
    /**
     * Deletes a product by its ID.
     *
     * @param id the ID of the product to delete
     */
    @Transactional
    public void deleteProduct(int id) {
        inventoryRepository.deleteInventoryByProductId(id);
        orderItemsRepository.deleteOrderItemByProductId(id);
        int rowsAffected = productsRepository.deleteById(id);
        if (rowsAffected > 0) {
            log.info("Product with ID {} deleted successfully", id);
        } else {
            log.warn("No product found with ID {}", id);
        }
    }

    /**
     * Retrieves all products in the database.
     *
     * @return list of products
     */
    public List<Products> getAllProducts() {
        List<Products> allProducts = productsRepository.findAll();
        log.info("Retrieved all products: {}", allProducts);
        return allProducts;
    }

    /**
     * Get product with order items.
     * @param productId the ID of the product to retrieve with order items
     * @return list of products with order items
     */
    public Products getProductWithOrderItems(int productId) {
        Products productWithOrderItems = productsRepository.getProductWithOrderItems(productId);
        log.info("Retrieved product with order items: {}", productWithOrderItems);
        return productWithOrderItems;
    }

    /**
     * Get products in stock.
     * @return list of products that are in stock
     */
    public List<Products> getProductsInStock() {
        List<Products> productsInStock = productsRepository.getProductsInStock();
        log.info("Retrieved products in stock: {}", productsInStock);
        return productsInStock;
    }
}
