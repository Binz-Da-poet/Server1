package com.example.ShopeeClone.auth;


import com.example.ShopeeClone.config.JwtService;
import com.example.ShopeeClone.entity.Role;
import com.example.ShopeeClone.entity.User;
import com.example.ShopeeClone.repositories.UserRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    @Autowired
    private UserRepositories userRepositories;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        Optional<User> foundUser = userRepositories.findByEmail(request.getEmail());
        if (foundUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                    AuthenticationResponse.builder().message("Email đã tồn tại ").build()
            );
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .address(request.getAddress())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(Role.ROLE_USER)
                .build();
        userRepositories.save(user);
        var jwtToken = jwtService.generateToken(user);
        return ResponseEntity.status(HttpStatus.OK).body(

                AuthenticationResponse.builder().message("Đăng ký thành công").token(jwtToken).user(user).build()
        );
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            User user = userRepositories.findByEmail(request.getEmail()).orElseThrow();
            var jwtToken= jwtService.generateToken(user);
            return ResponseEntity.status(HttpStatus.OK).body(
                    AuthenticationResponse.builder()
                            .token(jwtToken)
                            .message("Đăng nhập thành công")
                            .user(user)
                            .build()
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    AuthenticationResponse.builder()
                            .message("Email không tồn tại hoặc sai Mật khẩu ")
                            .build()
            );
        }
    }


}
