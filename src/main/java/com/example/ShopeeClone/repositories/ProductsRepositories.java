package com.example.ShopeeClone.repositories;
import com.example.ShopeeClone.entity.Category;
import com.example.ShopeeClone.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductsRepositories  extends JpaRepository<Product,Long> {


    Page<Product> findAllByCategory(Category category, Pageable pageable);


    Page<Product> findAllByCategoryAndPriceBetween(Category orElse, Integer price_min, Integer price_max, Pageable pageable);

    Page<Product> findAllByPriceBetween(Integer price_min, Integer price_max, Pageable pageable);

    Page<Product> findAllByCategoryAndRatingGreaterThanEqual(Category category, double rating, Pageable pageable);

    Page<Product> findAllByRatingGreaterThanEqual(double rating, Pageable pageable);

    Page<Product> findAllByCategoryAndNameContainingIgnoreCase(Category orElse, String name, Pageable pageable);

    Page<Product> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
}
