package com.example.ShopeeClone.repositories;

import com.example.ShopeeClone.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepositories extends JpaRepository<ShoppingCart,Long> {
}
