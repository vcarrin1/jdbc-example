package com.vcarrin87.jdbc_example.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vcarrin87.jdbc_example.models.Products;
import com.vcarrin87.jdbc_example.repository.InventoryRepository;
import com.vcarrin87.jdbc_example.repository.ProductsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductsService {

    @Autowired
    private ProductsRepository productsRepository;

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
        Products product = productsRepository.findById(id).orElse(null);
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
     * Deletes a product by its ID. Cascades to related order items via the
     * Products.orderItems mapping; inventory is unrelated so it's deleted explicitly.
     *
     * @param id the ID of the product to delete
     */
    @Transactional
    public void deleteProduct(int id) {
        inventoryRepository.deleteByProductId(id);
        if (productsRepository.existsById(id)) {
            productsRepository.deleteById(id);
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
     * Get product with order items. The lazy collection is initialized here,
     * while the transaction/session is still open.
     * @param productId the ID of the product to retrieve with order items
     * @return the product with its order items
     */
    @Transactional(readOnly = true)
    public Products getProductWithOrderItems(int productId) {
        Products product = productsRepository.findById(productId).orElse(null);
        if (product != null) {
            product.getOrderItems().size();
        }
        log.info("Retrieved product with order items: {}", product);
        return product;
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
