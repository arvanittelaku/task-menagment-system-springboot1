// src/main/java/com/example/taskmenagmentsystemspringboot1/service/impl/AuthenticationServiceImpl.java
package com.example.taskmenagmentsystemspringboot1.service.impl;

import com.example.taskmenagmentsystemspringboot1.security.AppUserDetails;
import com.example.taskmenagmentsystemspringboot1.service.AuthenticationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <-- ADD THIS IMPORT

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secretKey;

    private final Long expireTimeInMs = 86400000L; // 24 hours

    @Override
    @Transactional(readOnly = true) // <-- ADD THIS LINE
    public UserDetails authenticate(String email, String password) {
        System.out.println("Authenticating user: " + email);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        UserDetails details = userDetailsService.loadUserByUsername(email);
        System.out.println("User authenticated: " + details.getUsername());
        return details;
    }

    // ... (rest of the file is unchanged)
    @Override
    public String generateToken(UserDetails userDetails) {
        AppUserDetails appUser = (AppUserDetails) userDetails;

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", appUser.getId());
        claims.put("username", appUser.getUsername());
        claims.put("role", appUser.getRole());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(appUser.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeInMs))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    @Override
    public UserDetails validateToken(String token) {
        String email = extractUsername(token);
        return userDetailsService.loadUserByUsername(email);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }
}