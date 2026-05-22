package com.example.Status.of.application.repository;

import com.example.Status.of.application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByMobileNumber(String mobileNumber);
     static Optional<User> findByMobile(String mobile) {

         return Optional.empty();
     }
}
