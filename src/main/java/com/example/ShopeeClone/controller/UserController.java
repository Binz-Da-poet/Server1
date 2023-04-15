package com.example.ShopeeClone.controller;

import com.example.ShopeeClone.Service.UserService;
import com.example.ShopeeClone.entity.User;
import com.example.ShopeeClone.entity.ResponseObject;
import com.example.ShopeeClone.entity.Role;
import com.example.ShopeeClone.repositories.UserRepositories;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(path = "/api/v1/User")

public class UserController {

    @Autowired
    private UserRepositories userRepositories;
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepositories.findById(id);
        return user.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Query successfully", user)
                ) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "user not exist", "")
        );
    }

    @PostMapping("/addUser")
    public ResponseEntity<ResponseObject> addUser(@RequestBody User newUser) {
        Optional<User> foundUser = userRepositories.findAllByEmailAndPhoneNumber(newUser.getEmail(), newUser.getPhoneNumber());
        if (foundUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("failed", "Email or PhoneNumber is already", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Add Customer Successfully", userRepositories.save(newUser))
        );
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseObject> updateUser(@RequestBody User newUser) {
        User existingUser = userRepositories.findById(newUser.getUserId()).orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        existingUser.setFullName(newUser.getFullName());
        existingUser.setEmail(newUser.getEmail());
        existingUser.setPhoneNumber(newUser.getPhoneNumber());
        existingUser.setPassword(newUser.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "update successfully", userRepositories.save(existingUser))
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteUser(@PathVariable Long id) {
        User existingUser = userRepositories.findById(id).orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        userRepositories.delete(existingUser);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Delete successfully", existingUser)
        );
    }
}