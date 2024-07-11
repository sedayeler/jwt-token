package com.example.jwttoken.entites.dtos;

import com.example.jwttoken.entites.Role;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {
    String name;
    String username;
    String password;
    Role role;
}
