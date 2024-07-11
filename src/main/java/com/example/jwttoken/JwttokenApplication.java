package com.example.jwttoken;

import com.example.jwttoken.business.UserManager;
import com.example.jwttoken.entites.Role;
import com.example.jwttoken.entites.dtos.CreateUserDto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
        CreateUserDto user = CreateUserDto.builder()
                .name("yelerseda1")
                .username("yelerseda1")
                .password("12345")
                .role(Role.ADMIN)
                .build();
        userManager.createUser(user);
    }
}

