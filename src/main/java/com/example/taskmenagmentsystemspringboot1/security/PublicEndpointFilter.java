package com.example.taskmenagmentsystemspringboot1.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PublicEndpointFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        
        if (httpRequest.getMethod().equals(HttpMethod.POST.name()) && 
            (httpRequest.getRequestURI().equals("/api/v1/auth/login") || 
             httpRequest.getRequestURI().equals("/api/v1/auth/register"))) {
            chain.doFilter(request, response);
            return;
        }
        chain.doFilter(request, response);
    }
}
