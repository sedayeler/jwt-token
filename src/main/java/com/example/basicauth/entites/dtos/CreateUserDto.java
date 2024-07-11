package com.example.basicauth.entites.dtos;

import com.example.basicauth.entites.Role;
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
