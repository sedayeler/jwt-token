package com.example.jwttoken.entites.dtos;

import lombok.Data;

@Data
public class AuthDto {
    private String username;
    private String password;
}
