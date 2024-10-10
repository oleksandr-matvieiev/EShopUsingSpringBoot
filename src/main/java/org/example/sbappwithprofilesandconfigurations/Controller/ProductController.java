package org.example.sbappwithprofilesandconfigurations.Controller;

import org.example.sbappwithprofilesandconfigurations.Model.Product;
import org.example.sbappwithprofilesandconfigurations.Service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/get")
    public List<Product> getAllProducts() {
        return service.getAllProducts();
    }

    @PostMapping("/save")
    public ResponseEntity<Product> saveProduct(@RequestParam String name,
                                               @RequestParam int price,
                                               @RequestParam int quantity,
                                               @RequestParam String categoryName, // Зміна тут
                                               @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        Product savedProduct = service.saveProduct(name, price, quantity, categoryName, file);
        return ResponseEntity.ok(savedProduct);
    }

    @GetMapping("/category/{categoryName}")
    public List<Product> getProductsByCategory(@PathVariable String categoryName) {
        return service.getProductsByCategory(categoryName);
    }

    @GetMapping("/filter-by-price")
    public List<Product> filterProductsByPrice(@RequestParam int minPrice, @RequestParam int maxPrice) {
        return service.filterProductsByPrice(minPrice, maxPrice);
    }

    @PutMapping("/{productName}/quantity")
    public ResponseEntity<Product> updateProductQuantity(@PathVariable Long productName, @RequestParam int newQuantity) {
        Product updatedProduct = service.updateProductQuantity(productName, newQuantity);
        return ResponseEntity.ok(updatedProduct);
    }

}
