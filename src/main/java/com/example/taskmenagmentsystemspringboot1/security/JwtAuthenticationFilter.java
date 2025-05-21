package com.example.taskmenagmentsystemspringboot1.security;

import com.example.taskmenagmentsystemspringboot1.service.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j // per logging
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {

            // marrim tokenin prej header Authorization
            // Bearer dawkjdaklwdkalwdjakldjawkldjawkldjakldjawkldjkawkldja
            String token = extractTokenFromHeader(request);

            if (token != null) {
                var userDetails = authenticationService.validateToken(token);

                // E inicializon security context holderin
                var authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities() // rolet dhe permissions
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

                if (userDetails instanceof AppUserDetails) {
                    // per cdo request qe behet, shto user id ne request
                    request.setAttribute("userId", ((AppUserDetails) userDetails).getId());
                }
            }
        } catch (Exception e) {
            log.error("Error while processing JWT {}", e.getMessage());
        }

        // i tregon spring bootit qe me apliku filterin e krijuar
        filterChain.doFilter(request, response);
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        // Bearer dkwajldlawkdjkaldjkawjdawkldjal
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }

        return null;
    }
}
