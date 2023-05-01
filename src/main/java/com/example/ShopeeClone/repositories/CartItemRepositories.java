package com.example.ShopeeClone.repositories;

import com.example.ShopeeClone.entity.CartItem;
import com.example.ShopeeClone.entity.Product;
import com.example.ShopeeClone.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface CartItemRepositories extends JpaRepository<CartItem,Long> {
    Optional<CartItem> findByProductAndShoppingCartAndStatus(Product product, ShoppingCart shoppingCart,Integer Status);

    List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);

    ArrayList<CartItem> findByShoppingCartAndStatus(ShoppingCart shoppingCart, Integer status);
}