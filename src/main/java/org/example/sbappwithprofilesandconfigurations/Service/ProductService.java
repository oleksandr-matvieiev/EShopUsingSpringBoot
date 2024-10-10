package org.example.sbappwithprofilesandconfigurations.Service;

import org.example.sbappwithprofilesandconfigurations.Model.Category;
import org.example.sbappwithprofilesandconfigurations.Model.Product;
import org.example.sbappwithprofilesandconfigurations.Repo.CategoryRepo;
import org.example.sbappwithprofilesandconfigurations.Repo.ProductRepo;
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

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final String uploadDir = "uploads/";

    public ProductService(ProductRepo productRepo, CategoryRepo categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    public Product getProductByName(String name) {
        return productRepo.findByName(name);
    }

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Product saveProduct(String name, int price, int quantity, String categoryName, MultipartFile file) throws IOException {

        Product product = new Product();
        product.setName(cleanInput(name));
        product.setPrice(price);
        product.setQuantity(quantity);


        Category category = categoryRepo.findByName(cleanInput(categoryName)).orElseThrow(() -> new IllegalArgumentException("Category not found"));

        product.setCategory(category);

        if (file != null && !file.isEmpty()) {
            String imageUrl = saveImage(file);
            product.setImageUrl(imageUrl);
        }
        return productRepo.save(product);
    }

    private String saveImage(MultipartFile file) throws IOException {

        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDir + fileName);

        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        Files.write(path, file.getBytes());
        return "/uploads/" + fileName;
    }

    public List<Product> getProductsByCategory(String categoryName) {
        Category category = categoryRepo.findByName(categoryName).orElseThrow(() -> new IllegalArgumentException("Category not found"));
        return productRepo.findByCategory(category);
    }

    public List<Product> filterProductsByPrice(int minPrice, int maxPrice) {
        return productRepo.findByPriceBetween(minPrice, maxPrice);
    }

    public Product updateProductQuantity(Long productId, int newQuantity) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        product.setQuantity(newQuantity);
        return productRepo.save(product);
    }

    private String cleanInput(String input) {
        return input == null ? null : input.replace(",", "").trim();
    }

}
