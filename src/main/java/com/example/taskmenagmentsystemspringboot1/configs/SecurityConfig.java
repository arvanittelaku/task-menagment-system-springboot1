package com.example.taskmenagmentsystemspringboot1.configs;

import com.example.taskmenagmentsystemspringboot1.repositories.UserRepository;
import com.example.taskmenagmentsystemspringboot1.security.JwtAuthenticationFilter;
import com.example.taskmenagmentsystemspringboot1.security.PublicEndpointFilter;
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
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/users/me").authenticated() // Allow authenticated access to /users/me
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/register/user").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/register/manager").hasRole(ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/users/**").hasAnyRole(ADMIN.name(), MANAGER.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasAnyRole(ADMIN.name(), MANAGER.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/users/by-role").hasAnyRole(ADMIN.name(), MANAGER.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/users/managers").hasRole(ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasAnyRole(ADMIN.name(), MANAGER.name(), USER.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/tasks").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/tasks/{id}").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/tasks").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/tasks/create").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/tasks/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/tasks/{id}").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/tasks/{id}/status").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/tasks/my-tasks").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new PublicEndpointFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
