package org.example.sbappwithprofilesandconfigurations.Repo;

import org.example.sbappwithprofilesandconfigurations.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface CategoryRepo extends JpaRepository<Category, Long> {
    Optional<Category> findByName(@Param("name") String name);
}

