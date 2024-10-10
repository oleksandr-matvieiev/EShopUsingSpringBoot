package org.example.sbappwithprofilesandconfigurations.Repo;

import org.example.sbappwithprofilesandconfigurations.Model.Category;
import org.example.sbappwithprofilesandconfigurations.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);

    List<Product> findByPriceBetween(int minPrice, int maxPrice);

    Product findByName(String name);

}
