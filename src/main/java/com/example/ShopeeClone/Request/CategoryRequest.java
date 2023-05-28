package com.example.ShopeeClone.Request;

import com.example.ShopeeClone.entity.Category;
import com.example.ShopeeClone.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    private Product product;
    private Long CategoryID;
    private List<Long> CategoryIds;
}
