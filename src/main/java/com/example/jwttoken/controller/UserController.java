package com.example.jwttoken.controller;

import com.example.jwttoken.model.User;
import com.example.jwttoken.service.UserManager;
import com.example.jwttoken.dtos.responses.AuthResponse;
import com.example.jwttoken.dtos.requests.AuthRequest;
import com.example.jwttoken.dtos.requests.CreateUserRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class UserController {
    private final UserManager userManager;

    public UserController(UserManager userManager) {
        this.userManager = userManager;
    }

    @PostMapping("/register")
    public User register(@RequestBody CreateUserRequest createUserRequest) {
        return this.userManager.register(createUserRequest);
    }

    @PostMapping("/login")
    public AuthResponse authenticate(@RequestBody AuthRequest authRequest) {
        return this.userManager.authenticate(authRequest);
    }

    @PostMapping("/refreshtoken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        userManager.refreshToken(request, response);
    }
}
