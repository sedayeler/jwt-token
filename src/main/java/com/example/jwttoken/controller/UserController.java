package com.example.jwttoken.controller;

import com.example.jwttoken.business.JwtService;
import com.example.jwttoken.business.UserService;
import com.example.jwttoken.entites.User;
import com.example.jwttoken.entites.dtos.AuthDto;
import com.example.jwttoken.entites.dtos.CreateUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to JWT";
    }

    @PostMapping("/addNewUser")
    public User addNewUser(@RequestBody CreateUserDto createUserDto) {
        return this.userService.createUser(createUserDto);
    }

    @PostMapping("/generateToken")
    public String generateToken(@RequestBody AuthDto authDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authDto.getUsername());
        }
        log.info("Invalid username " + authDto.getUsername());
        throw new UsernameNotFoundException("Invalid username {}" + authDto.getUsername());
    }

    @GetMapping("/user")
    public String getUser() {
        return "This is a USER";
    }

    @GetMapping("/admin")
    public String getAdmin() {
        return "This is a ADMIN";
    }
}
