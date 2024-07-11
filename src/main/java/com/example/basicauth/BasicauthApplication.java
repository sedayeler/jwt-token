package com.example.basicauth;

import com.example.basicauth.business.UserManager;
import com.example.basicauth.entites.Role;
import com.example.basicauth.entites.dtos.CreateUserDto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BasicauthApplication implements CommandLineRunner {
    private UserManager userManager;

    public BasicauthApplication(UserManager userManager) {
        this.userManager = userManager;
    }

    public static void main(String[] args) {
        SpringApplication.run(BasicauthApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        createData();
    }

    private void createData() {
        CreateUserDto user = CreateUserDto.builder()
                .name("yelerseda")
                .username("yelerseda")
                .password("12345")
                .role(Role.ADMIN)
                .build();
        userManager.createUser(user);
    }
}

