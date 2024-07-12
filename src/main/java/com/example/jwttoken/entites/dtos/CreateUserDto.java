package com.example.jwttoken.entites.dtos;

import com.example.jwttoken.entites.Role;
import lombok.*;

@Data
@Builder
public class CreateUserDto {
    private String name;
    private String username;
    private String password;
    private Role role;
}
