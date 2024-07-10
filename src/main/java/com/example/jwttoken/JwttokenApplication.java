package com.example.jwttoken;

import com.example.jwttoken.business.UserManager;
import com.example.jwttoken.entites.Role;
import com.example.jwttoken.entites.User;
import com.example.jwttoken.entites.dtos.CreateUserDto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

@SpringBootApplication
public class JwttokenApplication implements CommandLineRunner {
    private UserManager userManager;

    public JwttokenApplication(UserManager userManager) {
        this.userManager = userManager;
    }

    public static void main(String[] args) {
        SpringApplication.run(JwttokenApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        createData();
    }

    private void createData() {
        Optional<User> existingUser = userManager.findByUsername("sedayeler");
        if (existingUser.isEmpty()) {
            CreateUserDto user = CreateUserDto.builder()
                    .name("Seda")
                    .username("sedayeler")
                    .password("abcd")
                    .role(Role.Role_Admin)
                    .build();
            userManager.createUser(user);
        }
    }
}
