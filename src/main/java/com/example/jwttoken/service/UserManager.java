package com.example.jwttoken.service;

import com.example.jwttoken.repository.UserDao;
import com.example.jwttoken.dtos.requests.AuthRequest;
import com.example.jwttoken.dtos.responses.AuthResponse;
import com.example.jwttoken.dtos.requests.CreateUserRequest;
import com.example.jwttoken.model.User;
import com.example.jwttoken.security.Config;
import com.example.jwttoken.token.Token;
import com.example.jwttoken.token.TokenDao;
import com.example.jwttoken.token.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserManager {
    private final UserDao userDao;
    private final JwtManager jwtManager;
    private final TokenDao tokenDao;
    private final AuthenticationManager authenticationManager;
    private final Config config;

    public UserManager(UserDao userDao, JwtManager jwtManager, TokenDao tokenDao, AuthenticationManager authenticationManager,
                       Config config) {
        this.userDao = userDao;
        this.jwtManager = jwtManager;
        this.tokenDao = tokenDao;
        this.authenticationManager = authenticationManager;
        this.config = config;
    }

    public User register(CreateUserRequest createUserRequest) {
        Optional<User> user = userDao.findByUsername(createUserRequest.getUsername());
        if (user.isPresent()) {
            throw new IllegalArgumentException("User already exists!");
        }

        User newUser = User.builder()
                .name(createUserRequest.getUsername())
                .username(createUserRequest.getUsername())
                .password(config.passwordEncoder().encode(createUserRequest.getPassword()))
                .role(createUserRequest.getRole())
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .isEnabled(true)
                .accountNonLocked(true)
                .build();
        return userDao.save(newUser);
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = userDao.findByUsername(request.getUsername()).orElseThrow();
        var jwtToken = jwtManager.generateToken(user.getUsername());
        var refreshToken = jwtManager.generateRefreshToken(user.getUsername());
        revokeAllUserTokens(user);
        saveToken(user, jwtToken);
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenDao.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenDao.findAllValidTokenByUser(user.getId());
        if (!validUserTokens.isEmpty()) {
            validUserTokens.forEach(token -> {
                token.setExpired(true);
                token.setRevoked(true);
            });
        }
        tokenDao.saveAll(validUserTokens);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if (authHeader != null || authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        username = jwtManager.extractUser(refreshToken);
        if (username != null) {
            var admin = this.userDao.findByUsername(username).orElseThrow();
            if (jwtManager.validateToken(refreshToken, admin)) {
                var accessToken = jwtManager.generateToken(admin.getUsername());
                revokeAllUserTokens(admin);
                saveToken(admin, accessToken);
                var authResponse = AuthResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}

