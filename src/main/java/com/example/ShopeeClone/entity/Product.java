package com.example.ShopeeClone.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="imageName")
    private String imageName;
    @Column(name="name",length = 350)
    private String name;
    @Column(name="price")
    private int price;
    @Column(name="discount_Price")
    private int discount_Price;
    @Column(name="rating")
    private double rating;
    @Column(name="sold")// tổng số lượng đã bán
    private int sold;
    @Column(name="quantity")
    private int quantity;
    @Column(name="view")
    private int view;
    @Column(name = "description",length = 3000)
    private String description;
    @Column(name="createdAt")
    private Date createdAt;
    @Column(name="updatedAt")
    private Date updatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")

    private Category category;

}
