package com.example.ShopeeClone.controller;

import com.example.ShopeeClone.entity.ResponseObject;
import com.example.ShopeeClone.entity.ShoppingCart;
import com.example.ShopeeClone.entity.User;
import com.example.ShopeeClone.repositories.ShoppingCartRepositories;
import com.example.ShopeeClone.repositories.UserRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/shopping-carts")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartRepositories shoppingCartRepository;
    @Autowired
    private UserRepositories userRepositories;

    @GetMapping("")
    public ResponseEntity<List<ShoppingCart>> getAllShoppingCarts() {
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findAll();
        return new ResponseEntity<>(shoppingCarts, HttpStatus.OK);
    }

    @PostMapping ("/shoppingCart")
    public ResponseEntity<ShoppingCart> getShoppingCartById(@RequestBody Long id) {
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findById(id);
        return shoppingCart.map(cart -> new ResponseEntity<>(cart, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @PostMapping("addShoppingCart")
    public ResponseEntity<ResponseObject> addShoppingCart(@RequestBody Long id) {
        Optional<User> findUser = userRepositories.findById(id);
        ShoppingCart shoppingCart = new ShoppingCart();
        if(findUser.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("flied ", " user ko tồn tại", "")
            );
        }
        shoppingCart.setUser(findUser.get());
        shoppingCartRepository.save(shoppingCart);
        Optional<User> user = userRepositories.findByShoppingCart(shoppingCart);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok ", " đã tạo shopping cart mới ", user.get())
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateShoppingCart(@PathVariable Long id, @RequestBody ShoppingCart shoppingCart) {
        Optional<ShoppingCart> existingShoppingCart = shoppingCartRepository.findById(id);
        if (existingShoppingCart.isPresent()) {
            shoppingCart.setId(id);
            shoppingCartRepository.save(shoppingCart);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShoppingCart(@PathVariable Long id) {
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findById(id);
        if (shoppingCart.isPresent()) {
            shoppingCartRepository.delete(shoppingCart.get());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
