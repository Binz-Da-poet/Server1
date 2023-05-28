package com.example.ShopeeClone.controller;

import com.example.ShopeeClone.Request.CartItemRequest;
import com.example.ShopeeClone.entity.*;
import com.example.ShopeeClone.repositories.CartItemRepositories;
import com.example.ShopeeClone.repositories.DeliveryCartRepositories;
import com.example.ShopeeClone.repositories.ProductsRepositories;
import com.example.ShopeeClone.repositories.ShoppingCartRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin
@RequestMapping(path = "/api/v1/DeliveryCart")
public class DeliveryCartController {

    @Autowired
    private ProductsRepositories productsRepositories;
    @Autowired
    private ShoppingCartRepositories shoppingCartRepositories;

    @Autowired
    private DeliveryCartRepositories deliveryCartRepositories;
    @Autowired
    protected CartItemRepositories cartItemRepositories;

    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addCartItem(@RequestBody CartItemRequest cartItemRequest) {
        Optional<ShoppingCart> findShoppingCart = shoppingCartRepositories.findById(cartItemRequest.getShoppingCartId());
        if (findShoppingCart.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("failed", "ShoppingCart does not exist", null));
        }

        ShoppingCart shoppingCart = findShoppingCart.get();
        List<DeliveryCart> itemsToAddDelivery = new ArrayList<>();

        for (Long productId : cartItemRequest.getProductIds()) {
            Optional<Product> findProduct = productsRepositories.findById(productId);

            if (findProduct.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("failed", "Product does not exist", null));
            }

            Optional<CartItem> findCartItem = cartItemRepositories.findByProductAndShoppingCart(findProduct.get(), shoppingCart);
            if (findCartItem.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("failed", "CartItem does not exist", null));
            }

            CartItem cartItem = findCartItem.get();
            Optional<DeliveryCart> findDeliveryCart = deliveryCartRepositories.findByProductAndShoppingCart(findProduct.get(), shoppingCart);
            DeliveryCart deliveryCart = new DeliveryCart();

            if (findDeliveryCart.isPresent()) {
                deliveryCart = findDeliveryCart.get();
                deliveryCart.setQuantity(deliveryCart.getQuantity() + cartItem.getQuantity());
                deliveryCart.setAddress(cartItemRequest.getAddress());
                deliveryCart.setPhoneNumber(cartItemRequest.getPhoneNumber());
                deliveryCart.setFullName(cartItemRequest.getFullName());
            } else {
                deliveryCart.setShoppingCart(cartItem.getShoppingCart());
                deliveryCart.setProduct(cartItem.getProduct());
                deliveryCart.setQuantity(cartItem.getQuantity());
                deliveryCart.setFullName(cartItemRequest.getFullName());
                deliveryCart.setPhoneNumber(cartItemRequest.getPhoneNumber());
                deliveryCart.setAddress(cartItemRequest.getAddress());
            }

            itemsToAddDelivery.add(deliveryCart);
            cartItemRepositories.delete(cartItem);
        }

        deliveryCartRepositories.saveAll(itemsToAddDelivery);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Đã thêm mục hàng giao hàng thành công", ""));
    }


}
