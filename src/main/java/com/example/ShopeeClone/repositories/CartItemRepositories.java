package com.example.ShopeeClone.repositories;

import com.example.ShopeeClone.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepositories extends JpaRepository<CartItem,Long> {
}
