package org.example.sbappwithprofilesandconfigurations.Service;

import org.example.sbappwithprofilesandconfigurations.Exception.CategoryNotFoundException;
import org.example.sbappwithprofilesandconfigurations.Exception.ProductNotFoundException;
import org.example.sbappwithprofilesandconfigurations.Model.Category;
import org.example.sbappwithprofilesandconfigurations.Model.Product;
import org.example.sbappwithprofilesandconfigurations.Repo.CategoryRepo;
import org.example.sbappwithprofilesandconfigurations.Repo.ProductRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ProductServiceTest {
    @Mock
    private ProductRepo productRepo;
    @Mock
    private CategoryRepo categoryRepo;
    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProductByName_Success() {
        Product mockProduct = new Product();
        mockProduct.setName("TestProduct");
        when(productRepo.findByName("TestProduct")).thenReturn(Optional.of(mockProduct));

        Product product = productService.getProductByName("TestProduct");
        assertEquals("TestProduct", product.getName());
        verify(productRepo, times(1)).findByName("TestProduct");
    }

    @Test
    void testGetProductByName_NotFound() {
        when(productRepo.findByName(anyString())).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.getProductByName("NonExistentProduct"));
        verify(productRepo, times(1)).findByName("NonExistentProduct");
    }

    @Test
    void testGetAllProducts() {
        Product product1 = new Product();
        product1.setName("TestProduct1");
        Product product2 = new Product();
        product2.setName("TestProduct2");
        when(productRepo.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> products = productService.getAllProducts();
        assertEquals(2, products.size());
        verify(productRepo, times(1)).findAll();
    }

    @Test
    void testSaveProduct_Success() throws IOException {
        String name = "TestProduct";
        String description = "Product description";
        double price = 100.0;
        int quantity = 10;
        String categoryName = "TestCategory";
        MultipartFile mockFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image".getBytes());

        Category mockCategory = new Category();
        mockCategory.setName(categoryName);
        when(categoryRepo.findByName(categoryName)).thenReturn(Optional.of(mockCategory));
        when(productRepo.save(any(Product.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        Product saveProduct = productService.saveProduct(name, description, price, quantity, categoryName, mockFile);

        assertEquals(name, saveProduct.getName());
        assertEquals(description, saveProduct.getDescription());
        assertEquals(price, saveProduct.getPrice());
        assertEquals(quantity, saveProduct.getQuantity());
        verify(categoryRepo, times(1)).findByName(categoryName);
        verify(productRepo, times(1)).save(any(Product.class));
    }

    @Test
    void testSaveProduct_CategoryNotFound() {
        String name = "TestProduct";
        String description = "Product description";
        double price = 100.0;
        int quantity = 10;
        String categoryName = "NonExistentCategory";
        MultipartFile mockFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image".getBytes());

        when(categoryRepo.findByName(categoryName)).thenReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class, () -> productService.saveProduct(name, description, price, quantity, categoryName, mockFile));
        verify(categoryRepo, times(1)).findByName(categoryName);
    }

    @Test
    void getProductsByCategory_Success() {
        String categoryName = "TestCategory";
        Category mockCategory = new Category();
        mockCategory.setName(categoryName);
        Product product = new Product();
        product.setName("TestProduct");

        when(categoryRepo.findByName(categoryName)).thenReturn(Optional.of(mockCategory));
        when(productRepo.findByCategory(mockCategory)).thenReturn(Collections.singletonList(product));

        List<Product> products = productService.getProductsByCategory(categoryName);
        assertEquals(1, products.size());
        assertEquals("TestProduct", products.get(0).getName());

        verify(categoryRepo, times(1)).findByName(categoryName);
        verify(productRepo, times(1)).findByCategory(mockCategory);
    }

    @Test
    void getProductsByCategory_CategoryNotFound() {
        String categoryName = "NonExistentCategory";

        when(categoryRepo.findByName(categoryName)).thenReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class, () -> productService.getProductsByCategory(categoryName));
        verify(categoryRepo, times(1)).findByName(categoryName);
    }

    @Test
    void filterProductsByPrice() {
        double minPrice = 50.0;
        double maxPrice = 100.0;
        Product product1 = new Product();
        product1.setPrice(75.0);
        Product product2 = new Product();
        product2.setPrice(80.0);

        when(productRepo.findByPriceBetween(minPrice, maxPrice)).thenReturn(Arrays.asList(product1, product2));
        List<Product> products = productService.filterProductsByPrice(minPrice, maxPrice);
        assertEquals(2, products.size());
        verify(productRepo, times(1)).findByPriceBetween(minPrice, maxPrice);
    }

    @Test
    void updateProductQuantity_Success() {
        long productId = 1L;
        int newQuantity = 20;
        Product product = new Product();
        product.setName("TestProduct");
        product.setId(productId);
        product.setQuantity(10);

        when(productRepo.findById(productId)).thenReturn(Optional.of(product));
        when(productRepo.save(product)).thenReturn(product);

        Product updatedProduct = productService.updateProductQuantity(productId, newQuantity);
        assertEquals(newQuantity, updatedProduct.getQuantity());

        verify(productRepo, times(1)).findById(productId);
        verify(productRepo, times(1)).save(product);
    }

    @Test
    void updateProductQuantity_ProductNotFound() {
        long productId = 1L;
        int newQuantity = 20;

        when(productRepo.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.updateProductQuantity(productId, newQuantity));
        verify(productRepo, times(1)).findById(productId);
    }

    @Test
    void getProductById_Success() {
        long productId = 1L;
        Product mockProduct = new Product();
        mockProduct.setId(productId);
        mockProduct.setName("TestProduct");

        when(productRepo.findById(productId)).thenReturn(Optional.of(mockProduct));
        Product product = productService.getProductById(productId);
        assertEquals("TestProduct", product.getName());
        verify(productRepo, times(1)).findById(productId);
    }

    @Test
    void getProductById_ProductNotFound() {
        long productId = 1L;
        when(productRepo.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(productId));

        verify(productRepo, times(1)).findById(productId);
    }

}
