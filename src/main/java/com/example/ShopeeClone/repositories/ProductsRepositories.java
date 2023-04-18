package com.example.ShopeeClone.repositories;
import com.example.ShopeeClone.entity.Category;
import com.example.ShopeeClone.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductsRepositories  extends JpaRepository<Product,Long> {


    Page<Product> findAllByCategory(Category category, Pageable pageable);

    Page<Product> findAllByCategoryAndRating(Category orElse, Double rating, Pageable pageable);

    Page<Product> findAllByRating(Double rating, Pageable pageable);


    Page<Product> findAllByCategoryAndPriceBetween(Category orElse, Integer price_min, Integer price_max, Pageable pageable);

    Page<Product> findAllByPriceBetween(Integer price_min, Integer price_max, Pageable pageable);

    Page<Product> findAllByCategoryAndRatingGreaterThanEqual(Category orElse, Integer rating, Pageable pageable);

    Page<Product> findAllByRatingGreaterThanEqual(Integer rating, Pageable pageable);
}
