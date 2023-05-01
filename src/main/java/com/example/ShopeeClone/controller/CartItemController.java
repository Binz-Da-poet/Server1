package com.example.ShopeeClone.controller;

import com.example.ShopeeClone.entity.*;
import com.example.ShopeeClone.repositories.CartItemRepositories;
import com.example.ShopeeClone.repositories.ProductsRepositories;
import com.example.ShopeeClone.repositories.ShoppingCartRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin
@RequestMapping(path = "/api/v1/CartItem")
public class CartItemController {
    @Autowired
    private ProductsRepositories productsRepositories;
    @Autowired
    private ShoppingCartRepositories shoppingCartRepositories;
    @Autowired
    protected CartItemRepositories cartItemRepositories;

    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addCartItem(@RequestBody CartItemRequest CartItemRequest) {
        Optional<Product> product = productsRepositories.findById(CartItemRequest.getProductId());
        Optional<ShoppingCart> shoppingCart = shoppingCartRepositories.findById(CartItemRequest.getShoppingCartId());
        Optional<CartItem> existsItem = cartItemRepositories.findByProductAndShoppingCartAndStatus(product.get(), shoppingCart.get(),CartItemRequest.getStatus());
        CartItem cartItem;
        if (existsItem.isPresent()) {
            cartItem = existsItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + CartItemRequest.getQuantity());
        } else {
            cartItem = new CartItem();
            cartItem.setShoppingCart(shoppingCart.get());
            cartItem.setProduct(product.get());
            cartItem.setQuantity(CartItemRequest.getQuantity());
            cartItem.setStatus(CartItemRequest.getStatus());
        }
        cartItemRepositories.save(cartItem);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok ", " đã add CartItem thanh cong ", cartItem)
        );
    }
    @PostMapping("/getCartItemStatus")
    public  ResponseEntity<ResponseObject> getCartItemByStatus(@RequestBody CartItemRequest cartItemRequest){
        Optional<ShoppingCart> shoppingCart = shoppingCartRepositories.findById(cartItemRequest.getShoppingCartId());
        ArrayList<CartItem> cartItems = cartItemRepositories.findByShoppingCartAndStatus(shoppingCart.get(),cartItemRequest.getStatus());
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok","Lấy cartItem thành công", cartItems)
        );
    }
    @PutMapping ("/update")
    public ResponseEntity<ResponseObject> UpdateCartItem(@RequestBody CartItemRequest CartItemRequest){
        Optional<Product> product = productsRepositories.findById(CartItemRequest.getProductId());
        Optional<ShoppingCart> shoppingCart = shoppingCartRepositories.findById(CartItemRequest.getShoppingCartId());
        Optional<CartItem> existsItem = cartItemRepositories.findByProductAndShoppingCartAndStatus(product.get(), shoppingCart.get(), CartItemRequest.getStatus());
        CartItem cartItem = existsItem.get();
        cartItem.setQuantity(CartItemRequest.getQuantity());
        cartItemRepositories.save(cartItem);
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok","Update thành công",cartItem)
        );
    }
    @PutMapping ("/updateStatus")
    public ResponseEntity<ResponseObject> UpdateCartItemStatus(@RequestBody CartItemRequest CartItemRequest ){
        Optional<ShoppingCart> shoppingCart = shoppingCartRepositories.findById(CartItemRequest.getShoppingCartId());
        if (!shoppingCart.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("error", "Không tìm thấy giỏ hàng", null)
            );
        }
        ArrayList<CartItem> cartItems = cartItemRepositories.findByShoppingCartAndStatus(shoppingCart.get(),1);
        List<CartItem> itemsToChangeStatus = new ArrayList<>();
        for (CartItem item : cartItems) {
           item.setStatus(2);
                itemsToChangeStatus.add(item);
        }


        cartItemRepositories.saveAll(itemsToChangeStatus);
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok","Update thành công",itemsToChangeStatus)
        );
    }
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseObject> deleteCartItem(@RequestBody CartItemRequest CartItemRequest){
        Optional<Product> product = productsRepositories.findById(CartItemRequest.getProductId());
        Optional<ShoppingCart> shoppingCart = shoppingCartRepositories.findById(CartItemRequest.getShoppingCartId());
        Optional<CartItem> existCartItem = cartItemRepositories.findByProductAndShoppingCartAndStatus(product.get(), shoppingCart.get(), CartItemRequest.getStatus());
        CartItem cartItem = existCartItem.get();
        cartItemRepositories.delete(cartItem);
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok","Delete thành công",cartItem)
        );
    }
    @DeleteMapping("/deleteMany")
    public ResponseEntity<ResponseObject> deleteManyCartItem(@RequestBody CartItemRequest CartItemRequest) {
        Optional<ShoppingCart> shoppingCart = shoppingCartRepositories.findById(CartItemRequest.getShoppingCartId());
        if (!shoppingCart.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("error", "Không tìm thấy giỏ hàng", null)
            );
        }

        List<CartItem> cartItems = cartItemRepositories.findByShoppingCart(shoppingCart.get());
        List<CartItem> itemsToDelete = new ArrayList<>();
        for (CartItem item : cartItems) {
            if (Arrays.asList(CartItemRequest.getProductIds()).contains(item.getProduct().getId())) {
                itemsToDelete.add(item);
            }
        }

        cartItemRepositories.deleteAll(itemsToDelete);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Delete thành công", null)
        );
    }

}
