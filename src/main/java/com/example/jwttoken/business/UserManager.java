package com.example.jwttoken.business;

import com.example.jwttoken.dataAccess.UserDao;
import com.example.jwttoken.entites.User;
import com.example.jwttoken.entites.dtos.CreateUserDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserManager {
    private UserDao userDao;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserManager(UserDao userDao, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDao = userDao;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Optional<User> findByUsername(String username) {
        return this.userDao.findByUsername(username);
    }

    public User createUser(CreateUserDto createUserDto) {
        Optional<User> existingUser = findByUsername(createUserDto.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        User newUser = User.builder()
                .name(createUserDto.getName())
                .username(createUserDto.getUsername())
                .password(bCryptPasswordEncoder.encode(createUserDto.getPassword()))
                .role(createUserDto.getRole())
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .isEnabled(true)
                .accountNonLocked(true)
                .build();
        return this.userDao.save(newUser);
    }
}
