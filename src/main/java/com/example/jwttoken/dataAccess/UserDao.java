package com.example.jwttoken.dataAccess;

import com.example.jwttoken.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDao extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
