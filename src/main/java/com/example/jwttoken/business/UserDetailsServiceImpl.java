package com.example.jwttoken.business;

import com.example.jwttoken.entites.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class UserDetailsServiceImpl implements UserDetailsService {
    private UserManager userManager;

    public UserDetailsServiceImpl(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userManager.findByUsername(username);
        return user.orElseThrow(EntityNotFoundException::new);
    }
}
