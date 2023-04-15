package com.example.ShopeeClone.repositories;

import com.example.ShopeeClone.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepositories extends JpaRepository<OrderDetail,Long> {
}
