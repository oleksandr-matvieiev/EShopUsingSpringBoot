package org.example.sbappwithprofilesandconfigurations.Repo;

import org.example.sbappwithprofilesandconfigurations.Model.Category;
import org.example.sbappwithprofilesandconfigurations.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);

    List<Product> findByPriceBetween(double minPrice, double maxPrice);

    Optional<Product> findByName(String name);
}
