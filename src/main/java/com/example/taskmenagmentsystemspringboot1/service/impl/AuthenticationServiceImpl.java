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
        private final Long expireTimeInMs = 86400000L; // 24h in milliseconds

        @Override
        public UserDetails authenticate(String email, String password) {
            // Authenticate the user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            // Load the user details
            return userDetailsService.loadUserByUsername(email);
        }

        @Override
        public String generateToken(UserDetails userDetails) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("authorities", userDetails.getAuthorities());
            claims.put("id", ((AppUserDetails) userDetails).getUser().getId());
            claims.put("username", ((AppUserDetails) userDetails).getUser().getUsername());
            claims.put("role", ((AppUserDetails) userDetails).getUser().getRole().name());

            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(((AppUserDetails) userDetails).getUser().getEmail()) // FIXED HERE
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
