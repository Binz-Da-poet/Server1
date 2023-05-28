package com.example.ShopeeClone.repositories;

import com.example.ShopeeClone.entity.DeliveryCart;
import com.example.ShopeeClone.entity.Product;
import com.example.ShopeeClone.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryCartRepositories extends JpaRepository<DeliveryCart, Long> {

    Optional<DeliveryCart> findByProductAndShoppingCart(Product product ,ShoppingCart shoppingCart);

}
