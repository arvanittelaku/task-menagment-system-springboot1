package com.example.taskmenagmentsystemspringboot1.configs;

import com.example.taskmenagmentsystemspringboot1.entities.user.UserRole;
import com.example.taskmenagmentsystemspringboot1.entities.user.User;
import com.example.taskmenagmentsystemspringboot1.repositories.UserRepository;
import com.example.taskmenagmentsystemspringboot1.security.AppUserDetailsService;
import com.example.taskmenagmentsystemspringboot1.security.JwtAuthenticationFilter;
import com.example.taskmenagmentsystemspringboot1.service.AuthenticationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.example.taskmenagmentsystemspringboot1.security.PublicEndpointFilter;

import static com.example.taskmenagmentsystemspringboot1.entities.user.Permission.*;
import static com.example.taskmenagmentsystemspringboot1.entities.user.UserRole.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserRepository userRepository;

    @Autowired
    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationService authenticationService) {
        return new JwtAuthenticationFilter(authenticationService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                                // Public endpoints (no authentication required)
                                .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/auth/register").permitAll()


                                // User management endpoints (admin only)
                                .requestMatchers(HttpMethod.POST, "/api/v1/").hasRole(ADMIN.name())
                                .requestMatchers(HttpMethod.GET, "/api/v1/managers").hasRole(ADMIN.name())
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/{id}").hasRole(ADMIN.name())
                                .requestMatchers(HttpMethod.POST, "/api/v1/register/manager").hasRole(ADMIN.name())
                                .requestMatchers(HttpMethod.GET, "/api/v1/{id}").hasRole(ADMIN.name())
                                .requestMatchers(HttpMethod.GET, "/api/v1/by-role").hasRole(ADMIN.name())
                                .requestMatchers(HttpMethod.GET, "/api/v1/username/{username}").hasRole(ADMIN.name())
                                .requestMatchers(HttpMethod.PUT, "/api/v1/").hasRole(ADMIN.name())
                                .requestMatchers(HttpMethod.POST, "/api/v1/user/register").hasRole(ADMIN.name())

                                // Task management endpoints
                                // CRUD operations - admin only
                                .requestMatchers(HttpMethod.POST, "/tasks").hasRole(ADMIN.name())
                                .requestMatchers(HttpMethod.PUT, "/tasks/{id}").hasRole(ADMIN.name())
                                .requestMatchers(HttpMethod.DELETE, "/tasks/{id}").hasRole(ADMIN.name())
                                
                                // Task status updates - manager and admin
                                .requestMatchers(HttpMethod.PATCH, "/tasks/status/{taskId}").hasAnyRole(ADMIN.name(), MANAGER.name())
                                
                                // Task assignment - admin only
                                .requestMatchers(HttpMethod.POST, "/tasks/assign/{taskId}/{userId}").hasRole(ADMIN.name())
                                
                                // Current user tasks - any authenticated user
                                .requestMatchers(HttpMethod.GET, "/tasks/current-user/{user-id}").authenticated()
                                
                                // Get task by ID - any authenticated user
                                .requestMatchers(HttpMethod.GET, "/tasks/{id}").authenticated()
                                
                                // All other requests require authentication
                                .anyRequest().authenticated()
                )
                .addFilterBefore(new PublicEndpointFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("password"))
                .email("user@example.com")
                .role(UserRole.ADMIN)
                .build();

        var manager = User.builder()
                .username("manager")
                .password(passwordEncoder().encode("password"))
                .email("manager@example.com")
                .role(UserRole.MANAGER)
                .build();

        var admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("password"))
                .email("admin@example.com")
                .role(UserRole.ADMIN)
                .build();

        return new AppUserDetailsService(userRepository, user, manager, admin);
    }
}