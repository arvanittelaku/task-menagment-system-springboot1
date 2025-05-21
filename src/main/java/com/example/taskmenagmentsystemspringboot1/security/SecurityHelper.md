# Security Documentation

## 1. SecurityConfig.java Explanation

This code configures Spring Security for a web application. It defines beans and a security filter chain:

- **@Configuration**: Marks this class as a configuration component for Spring.
- **@EnableWebSecurity**: Enables Spring Security's web security support.

### Defined Beans
1. **AuthenticationManager**:
   ```java
   @Bean
   public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
       return config.getAuthenticationManager();
   }
   ```
   This bean provides authentication logic. It retrieves the `AuthenticationManager` from `AuthenticationConfiguration`.

2. **PasswordEncoder**:
   ```java
   @Bean
   public PasswordEncoder passwordEncoder() {
       return PasswordEncoderFactories.createDelegatingPasswordEncoder();
   }
   ```
   Creates a password encoder using Spring's `PasswordEncoderFactories`. This delegates to multiple encoding strategies (e.g., BCrypt).

3. **SecurityFilterChain**:
   ```java
   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       http
           .authorizeHttpRequests(auth -> auth
               .requestMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll()
               .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").permitAll()
               .requestMatchers(HttpMethod.GET, "/api/v1/tags/**").permitAll()
               .anyRequest().authenticated()
           )
           .csrf(csrf -> csrf.disable())
           .sessionManagement(session ->
               session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
           );

       return http.build();
   }
   ```
   Configures security rules using the `HttpSecurity` object:
    - Allows GET requests to `/api/v1/posts/**`, `/api/v1/categories/**`, and `/api/v1/tags/**` without authentication.
    - Requires authentication for all other requests.
    - Disables CSRF protection since the app likely uses JWT (stateless).
    - Configures stateless session management with `SessionCreationPolicy.STATELESS`.

The `SecurityFilterChain` bean is essential for customizing access control rules.

---

## 2. BlogUserDetails.java Explanation

This class implements Spring Security's `UserDetails` interface to represent authenticated users.

### Key Elements
- **Fields**:
  ```java
  private final User user;
  ```
  Represents the application's `User` entity (contains user details like email, password, etc.).

### Implemented Methods
1. **getAuthorities**:
   ```java
   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
       return List.of(new SimpleGrantedAuthority("ROLE_USER"));
   }
   ```
   Returns the roles/permissions granted to the user. Currently, it provides a single authority: `"ROLE_USER"`.

2. **getPassword**:
   ```java
   @Override
   public String getPassword() {
       return user.getPassword();
   }
   ```
   Returns the encoded password of the user from the `User` entity.

3. **getUsername**:
   ```java
   @Override
   public String getUsername() {
       return user.getEmail();
   }
   ```
   Returns the user's email as the username.

4. **isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled**:
   ```java
   @Override
   public boolean isAccountNonExpired() { return true; }

   @Override
   public boolean isAccountNonLocked() { return true; }

   @Override
   public boolean isCredentialsNonExpired() { return true; }

   @Override
   public boolean isEnabled() { return true; }
   ```
   Always return `true`, meaning the user's account has no restrictions.

5. **getId**:
   ```java
   public UUID getId() {
       return user.getId();
   }
   ```
   A custom method that retrieves the `UUID` of the user.

This class integrates application-specific user details with Spring Security's authentication framework.

---

## 3. AuthenticationServiceImpl.java Explanation

This class implements an `AuthenticationService` to handle JWT-based authentication.

### Key Methods
1. **authenticate(email, password)**:
   ```java
   @Override
   public UserDetails authenticate(String email, String password) {
       authenticationManager.authenticate(
           new UsernamePasswordAuthenticationToken(email, password)
       );
       return userDetailsService.loadUserByUsername(email);
   }
   ```
   Uses `AuthenticationManager` to validate user credentials. If successful, loads user details using `UserDetailsService`.

2. **generateToken(UserDetails userDetails)**:
   ```java
   @Override
   public String generateToken(UserDetails userDetails) {
       Map<String, Object> claims = new HashMap<>();
       return Jwts.builder()
           .setClaims(claims)
           .setSubject(userDetails.getUsername())
           .setIssuedAt(new Date(System.currentTimeMillis()))
           .setExpiration(new Date(System.currentTimeMillis() + jwtExpiryMs))
           .signWith(getSigningKey(), SignatureAlgorithm.HS256)
           .compact();
   }
   ```
   Creates a JWT token containing the user's username and expiration time (24 hours). Signs the token with an HMAC SHA-256 algorithm and a secret key.

3. **validateToken(String token)**:
   ```java
   @Override
   public UserDetails validateToken(String token) {
       String username = extractUsername(token);
       return userDetailsService.loadUserByUsername(username);
   }
   ```
   Parses the token and extracts the username. Loads user details from the database.

### Utility Methods
- **extractUsername**:
   ```java
   private String extractUsername(String token) {
       Claims claims = Jwts.parserBuilder()
           .setSigningKey(getSigningKey())
           .build()
           .parseClaimsJws(token)
           .getBody();
       return claims.getSubject();
   }
   ```
  Extracts the username from JWT claims.

- **getSigningKey**:
   ```java
   private Key getSigningKey() {
       byte[] keyBytes = secretKey.getBytes();
       return Keys.hmacShaKeyFor(keyBytes);
   }
   ```
  Generates a cryptographic signing key from the secret key (`@Value("${jwt.secret}")`).

This service handles the full lifecycle of authentication, from validation to token generation.

---

## 4. JwtAuthenticationFilter.java Explanation

This class is a custom filter for JWT authentication. It extends `OncePerRequestFilter` to ensure it runs once per request.

### Key Responsibilities
1. Extract JWT token from the `Authorization` header:
   ```java
   private String extractToken(HttpServletRequest request) {
       String bearerToken = request.getHeader("Authorization");
       if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
           return bearerToken.substring(7);
       }
       return null;
   }
   ```

2. Validate the token with `AuthenticationService.validateToken`.

3. If valid, create a `UsernamePasswordAuthenticationToken` and set it in the `SecurityContext`:
   ```java
   UsernamePasswordAuthenticationToken authentication =
       new UsernamePasswordAuthenticationToken(
           userDetails,
           null,
           userDetails.getAuthorities()
       );
   SecurityContextHolder.getContext().setAuthentication(authentication);
   ```

4. Add the user's ID as a request attribute (`userId`) for downstream controllers.
5. Handle invalid tokens by logging a warning and proceeding without authentication:
   ```java
   log.warn("Received invalid auth token");
   ```

This filter is typically registered before `UsernamePasswordAuthenticationFilter` in Spring Security.

---

## 5. How is this Used?
This filter is typically registered in the Spring Security configuration:

 ```java
 @Bean
 public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/public/**").permitAll()
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .csrf(AbstractHttpConfigurer::disable);
    
    return http.build();
 }
 ```

- This registers JwtAuthenticationFilter before UsernamePasswordAuthenticationFilter.
- Ensures JWT authentication runs before username-password authentication.

---

## 6. General Workflow of the Code

- The `SecurityConfig` sets up security rules and the JWT authentication filter.
- `BlogUserDetails` bridges the application's user entity with Spring Security's `UserDetails`.
- `AuthenticationServiceImpl` handles JWT generation, validation, and user authentication.
- `JwtAuthenticationFilter` ensures that every request with a valid JWT is authenticated and integrated into Spring Security's context.

Together, these components provide robust, JWT-based security for a Spring Boot application.