package com.example.ShopeeClone.repositories;

import com.example.ShopeeClone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepositories extends JpaRepository<User,Long> {
   Optional<User> findAllByEmailAndPhoneNumber(String email, String phoneNumber);
   Optional<User> findByEmail(String email);

    Optional<User> findByFullNameAndPassword(String fullName,String PassWord);
}
