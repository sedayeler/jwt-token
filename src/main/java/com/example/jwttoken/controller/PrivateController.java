package com.example.jwttoken.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private")
public class PrivateController {
    @GetMapping("/user")
    public String getUser() {
        return "This is a USER";
    }

    @GetMapping("/admin")
    public String getAdmin() {
        return "This is a ADMIN";
    }
}
