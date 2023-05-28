package com.example.ShopeeClone.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRequest {
    private Long productId;
    private Long shoppingCartId;
    private int quantity;
    private String address;
    private String phoneNumber;
    private String fullName;
    private Long[] productIds;
}
