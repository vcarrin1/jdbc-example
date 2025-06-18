package com.vcarrin87.jdbc_example.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vcarrin87.jdbc_example.models.Products;
import com.vcarrin87.jdbc_example.services.ProductsService;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductsService productsService;
    /*
     * This method is used to create a new product.
     * Example of a POST request:
     {
         "name": "Product A",
         "description": "Description of Product A",
         "price": 19.99
     }
     */
    @PostMapping("/new-product")
    public void createProduct(@RequestBody Products product) {
        productsService.createProduct(product);
    }
    /*
     * This method is used to get all products.
     * Example of a GET request:
     /products
     */ 
    @GetMapping("/all-products")
    public List<Products> getAllProducts() {
        return productsService.getAllProducts();
    }

    /*
     * This method is used to get a product by its ID.
     * Example of a GET request:
     /products/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<Products> getProductById(@PathVariable int id) {
        Products product = productsService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /*
     * This method is used to update a product.
     * Example of a PUT request:
     {
         "productId": 1,
         "name": "Updated Product A",
         "description": "Updated description of Product A",
         "price": 29.99
     }
     */
    @PostMapping("/update-product")
    public ResponseEntity<String> updateProduct(@RequestBody Products product) {
        try {
           productsService.updateProduct(product);
           return ResponseEntity.ok("Product updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating product: " + e.getMessage());
        }
    }
    /*
     * This method is used to delete a product by its ID.
     * Example of a DELETE request:
     /products/delete-product/1
     */
    @DeleteMapping("/delete-product/{product_id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int product_id) {
        try {
            productsService.deleteProduct(product_id);
            return ResponseEntity.ok("Product " + product_id + " deleted successfully");
        } catch (Exception e) {
            System.out.println("Error deleting product: " + e.getMessage());
            return ResponseEntity.status(500).body("Error deleting product: " + e.getMessage());
        }
    }
    
    /*
     * This method retrieves all products that are in stock.
     * Example of a GET request:
     /products/in-stock
     */
    @GetMapping("/in-stock")
    public List<Products> getProductsInStock() {
        return productsService.getProductsInStock();
    }
    /*
     * This method retrieves all products with their order items.
     * Example of a GET request:
     /products/with-order-items?productId=1
     */
    @GetMapping("/with-order-items")
    public Products getProductsWithOrderItems(@RequestParam int productId) {
        return productsService.getProductWithOrderItems(productId);
    }
}
