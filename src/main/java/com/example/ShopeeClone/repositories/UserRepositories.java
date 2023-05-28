package com.example.ShopeeClone.repositories;

import com.example.ShopeeClone.entity.ShoppingCart;
import com.example.ShopeeClone.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepositories extends JpaRepository<User,Long> {

   Optional<User> findByEmail(String email);


    Optional<User> findByShoppingCart(ShoppingCart shoppingCart);

    List<User> findByFullNameContainingIgnoreCase(String name);
}
