package com.example.ShopeeClone.controller;


import com.example.ShopeeClone.config.JwtService;
import com.example.ShopeeClone.entity.ChangePasswordRequest;
import com.example.ShopeeClone.entity.ResponseObject;
import com.example.ShopeeClone.entity.User;
import com.example.ShopeeClone.repositories.UserRepositories;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import java.util.Objects;
import java.util.Optional;


@Controller
@CrossOrigin
@RequestMapping(path = "/api/v1/User" )

public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepositories userRepositories;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/me" )
    public ResponseEntity<ResponseObject> getUser(HttpServletRequest request) {
        try {
            UserDetails userDetails = jwtService.authenticateUser(request);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "lấy profile thành công", userDetails)
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    new ResponseObject("failed", "sai token", "" )
            );
        }
    }

    @PostMapping("/update" )
    public ResponseEntity<ResponseObject> updateUser(@RequestBody User newUser, HttpServletRequest request) {
        try {
            User user = jwtService.authenticateUser(request);
            if (Objects.nonNull(newUser.getPhoneNumber())) {
                user.setPhoneNumber(newUser.getPhoneNumber());
            }
            if (Objects.nonNull(newUser.getFullName())) {
                user.setFullName(newUser.getFullName());
            }
            if (Objects.nonNull(newUser.getAddress())) {
                user.setAddress(newUser.getAddress());
            }
            if (Objects.nonNull(newUser.getAvatar())) {
                user.setAvatar(newUser.getAvatar());
            }

            userRepositories.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Cập nhật thông tin thành công", user)
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    new ResponseObject("failed", "Token không hợp lệ", "" )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("failed", "Lỗi server", "" )
            );
        }
    }


    @PostMapping("/changePassword")
    public ResponseEntity<ResponseObject> changePassWord(@RequestBody ChangePasswordRequest changePasswordRequest, HttpServletRequest request) {
        Optional<User> user = Optional.of(new User());
        try {
            UserDetails userDetails = jwtService.authenticateUser(request);
            user = userRepositories.findByEmail(userDetails.getUsername());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObject("failed", "sai token", ""));
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.get().getUsername(), changePasswordRequest.getPassword()
            ));




            User newUser = user.get();
            newUser.setPassword(passwordEncoder.encode(changePasswordRequest.getNew_password()));
            userRepositories.save(newUser);

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "thay đổi mật khẩu thành công", ""));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject("failed", "sai mật khẩu", ""));
        }
    }


    @DeleteMapping("/delete/{id}" )
    public ResponseEntity<ResponseObject> deleteUser(@PathVariable Long id) {
        User existingUser = userRepositories.findById(id).orElseThrow(() -> new EntityNotFoundException("Customer not found" ));
        userRepositories.delete(existingUser);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Delete successfully", existingUser)
        );
    }
}