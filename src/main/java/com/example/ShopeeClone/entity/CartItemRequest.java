package com.example.ShopeeClone.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRequest {
    private  Long productId;
    private Long shoppingCartId;
    private int quantity;
    private int status;
    private Long[] productIds;
}
