package org.example.sbappwithprofilesandconfigurations.Service;

import org.example.sbappwithprofilesandconfigurations.Exception.CategoryNotFoundException;
import org.example.sbappwithprofilesandconfigurations.Exception.ProductNotFoundException;
import org.example.sbappwithprofilesandconfigurations.Model.Category;
import org.example.sbappwithprofilesandconfigurations.Model.Product;
import org.example.sbappwithprofilesandconfigurations.Repo.CategoryRepo;
import org.example.sbappwithprofilesandconfigurations.Repo.ProductRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;

    public ProductService(ProductRepo productRepo, CategoryRepo categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    public Product getProductByName(String name) {
        return productRepo.findByName(name)
                .orElseThrow(() -> {
                    logger.error("Product not found with name: {}", name);
                    return new ProductNotFoundException("Invalid name or product dont exists");
                });
    }

    public List<Product> getAllProducts() {
        logger.info("Fetching all products");
        return productRepo.findAll();
    }

    public Product saveProduct(String name, String description, double price, int quantity, String categoryName, MultipartFile file) throws IOException {
        logger.info("Saving product with name: {}", name);
        Product product = new Product();
        product.setName(cleanInput(name));
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantity(quantity);


        Category category = categoryRepo.findByName(cleanInput(categoryName))
                .orElseThrow(() -> {
                    logger.error("Category not found with name: {}", cleanInput(categoryName));
                    return new CategoryNotFoundException("Category not found");
                });

        product.setCategory(category);

        if (file != null && !file.isEmpty()) {
            String imageUrl = saveImage(file);
            product.setImageUrl("/uploads/" + imageUrl);
        }
        logger.info("Product {} was saved", name);
        return productRepo.save(product);
    }

    private String saveImage(MultipartFile file) throws IOException {

        if (!file.getContentType().startsWith("image/")) {
            logger.error("Invalid file type: {}. Only images allowed.", file.getContentType());
            throw new IllegalArgumentException("Only image files are allowed");
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String uploadDir = "src/main/resources/static/uploads/";
        Path path = Paths.get(uploadDir + fileName);

        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        Files.write(path, file.getBytes());
        logger.info("Image {} uploaded successfully", fileName);
        return fileName;
    }

    public List<Product> getProductsByCategory(String categoryName) {
        Category category = categoryRepo.findByName(categoryName)
                .orElseThrow(() -> {
                    logger.error("Category not found with name: {} ", categoryName);
                    return new CategoryNotFoundException("Category not found");
                });
        logger.info("Fetching products by category: {}", categoryName);
        return productRepo.findByCategory(category);
    }

    public List<Product> filterProductsByPrice(double minPrice, double maxPrice) {
        logger.info("Filtering products by price range: {} - {}", minPrice, maxPrice);
        return productRepo.findByPriceBetween(minPrice, maxPrice);
    }

    public Product updateProductQuantity(Long productId, int newQuantity) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> {
                    logger.error("Product not found with ID: {}", productId);
                    return new ProductNotFoundException("Product not found");
                });
        product.setQuantity(newQuantity);
        logger.info("Updated quantity of product {} to {}", product.getName(), newQuantity);
        return productRepo.save(product);
    }

    private String cleanInput(String input) {
        return input == null ? null : input.replace(",", "").trim();
    }

    public Product getProductById(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> {
                    logger.error("Product not found with ID: {}", id);
                    return new ProductNotFoundException("Product not found");
                });
    }
}
