package com.example.ShopeeClone.repositories;

import com.example.ShopeeClone.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepositories extends JpaRepository<Order,Long> {

}
