package org.example.sbappwithprofilesandconfigurations.Controller;

import org.example.sbappwithprofilesandconfigurations.Model.Product;
import org.example.sbappwithprofilesandconfigurations.Service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductService service;
    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/get")
    public List<Product> getAllProducts() {
        logger.info("Fetching all products");
        return service.getAllProducts();
    }

    @PostMapping("/save")
    public ResponseEntity<Product> saveProduct(@RequestParam String productName,
                                               @RequestParam double price,
                                               @RequestParam int quantity,
                                               @RequestParam String categoryName,
                                               @RequestParam(value = "file", required = false) MultipartFile file) {
        logger.info("Saving product with name: {}", productName);
        try {
            Product savedProduct = service.saveProduct(productName, price, quantity, categoryName, file);
            logger.info("Product {} saved successfully ", productName);
            return ResponseEntity.ok(savedProduct);
        } catch (Exception e) {
            logger.error("Error while saving product {}: {}", productName, e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/category/{categoryName}")
    public List<Product> getProductsByCategory(@PathVariable String categoryName) {
        logger.info("Fetching products by category: {}", categoryName);
        return service.getProductsByCategory(categoryName);
    }

    @GetMapping("/filter-by-price")
    public List<Product> filterProductsByPrice(@RequestParam double minPrice, @RequestParam double maxPrice) {
        logger.info("Filtering products by price range: {} - {}", minPrice, maxPrice);
        return service.filterProductsByPrice(minPrice, maxPrice);
    }

    @PutMapping("/{productName}/quantity")
    public ResponseEntity<Product> updateProductQuantity(@PathVariable Long productName, @RequestParam int newQuantity) {
        logger.info("Updating quantity for product with name: {}", productName);

        try {
            Product updatedProduct = service.updateProductQuantity(productName, newQuantity);
            logger.info("Product quantity updated successfully for product {}", productName);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            logger.error("Error while updating quantity for product {}", productName);
            return ResponseEntity.status(500).body(null);
        }
    }

}
