package com.example.jwttoken.security;

import com.example.jwttoken.service.JwtManager;
import com.example.jwttoken.token.TokenDao;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtManager jwtManager;
    private final UserDetailsService userDetailsService;
    private final TokenDao tokenDao;

    public JwtAuthFilter(JwtManager jwtManager, UserDetailsService userDetailsService, TokenDao tokenDao) {
        this.jwtManager = jwtManager;
        this.userDetailsService = userDetailsService;
        this.tokenDao = tokenDao;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String userName = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            userName = jwtManager.extractUser(token);
        }
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails admin = userDetailsService.loadUserByUsername(userName);
            var isTokenValid = tokenDao.findByToken(token)
                    .map(t -> !t.isExpired() && !t.isRevoked()).orElse(false);
            if (jwtManager.validateToken(token, admin) && isTokenValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(admin, null, admin.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
