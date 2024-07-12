package com.example.jwttoken.business;

import com.example.jwttoken.dataAccess.UserDao;
import com.example.jwttoken.entites.User;
import com.example.jwttoken.entites.dtos.CreateUserDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserDao userDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserDao userDao, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDao = userDao;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userDao.findByUsername(username);
        return user.orElseThrow(EntityNotFoundException::new);
    }

    public Optional<User> findByUsername(String username) {
        return this.userDao.findByUsername(username);
    }

    public User createUser(CreateUserDto createUserDto) {
        Optional<User> existingUser = findByUsername(createUserDto.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        User newUser = User.builder()
                .name(createUserDto.getName())
                .username(createUserDto.getUsername())
                .password(bCryptPasswordEncoder.encode(createUserDto.getPassword()))
                .role(createUserDto.getRole())
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .isEnabled(true)
                .accountNonLocked(true)
                .build();
        return this.userDao.save(newUser);
    }
}
