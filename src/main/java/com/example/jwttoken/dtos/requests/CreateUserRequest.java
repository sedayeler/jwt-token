package com.example.jwttoken.dtos.requests;

import com.example.jwttoken.model.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {
    private String name;
    private String username;
    private String password;
    private Role role;
}
