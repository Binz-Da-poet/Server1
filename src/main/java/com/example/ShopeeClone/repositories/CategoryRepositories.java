package com.example.ShopeeClone.repositories;

import com.example.ShopeeClone.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepositories extends JpaRepository<Category,Long> {
    Optional<Category> findByName(String name);
    Optional<Category> findById(Long id);
}
