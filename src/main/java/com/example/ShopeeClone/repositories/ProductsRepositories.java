package com.example.ShopeeClone.repositories;
import com.example.ShopeeClone.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductsRepositories  extends JpaRepository<Product,Long> {


}
