package com.example.jwttoken.entites;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public enum Role implements GrantedAuthority {
    Role_User(1, "USER"),
    Role_Admin(2, "ADMIN");

    private int id;
    private String roleName;

    Role(int id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    @Override
    public String getAuthority() {
        return roleName;
    }
}
