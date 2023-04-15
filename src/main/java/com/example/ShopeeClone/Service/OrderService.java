package com.example.ShopeeClone.Service;

import com.example.ShopeeClone.entity.Order;
import com.example.ShopeeClone.entity.ResponseObject;
import com.example.ShopeeClone.repositories.OrderRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;


@Service
public class OrderService {
    @Autowired
    private OrderRepositories orderRepositories;


}
