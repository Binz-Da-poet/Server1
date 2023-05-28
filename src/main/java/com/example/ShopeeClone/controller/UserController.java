package com.example.ShopeeClone.controller;


import com.example.ShopeeClone.config.JwtService;
import com.example.ShopeeClone.Request.ChangePasswordRequest;
import com.example.ShopeeClone.entity.ResponseObject;
import com.example.ShopeeClone.entity.Status;
import com.example.ShopeeClone.entity.User;
import com.example.ShopeeClone.repositories.UserRepositories;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import java.util.List;
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

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}" )
    public ResponseEntity<ResponseObject> deleteUser(@PathVariable Long id) {
        User existingUser = userRepositories.findById(id).orElseThrow(() -> new EntityNotFoundException("Customer not found" ));
        userRepositories.delete(existingUser);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Delete successfully", existingUser)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/Admin/getAllUser")
    public ResponseEntity<ResponseObject> getAllUser(
            @RequestParam(required = false) String name,
            Pageable pageable
    ) {
        List<User> userList;
        if (name != null && !name.isEmpty()) {
            userList = userRepositories.findByFullNameContainingIgnoreCase(name);
        } else {
            userList = userRepositories.findAll();
        }

        Page<User> userPage = new PageImpl<>(userList, pageable, userList.size());

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Get ALL User success", userPage)
        );
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/Admin/State/{id}")
    public ResponseEntity<ResponseObject> changeState(@PathVariable Long id) {
        Optional<User> findUser = userRepositories.findById(id);
        if (findUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "User ko ton tai", "")
            );
        }
        User user = findUser.get();
        user.setStatus(user.getStatus() == Status.NonLocked ? Status.Locked : Status.NonLocked);
        userRepositories.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "đôi state thành công", user)
        );
    }
}