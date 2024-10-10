package com.example.SpringSecurityDemo.config;

import com.example.SpringSecurityDemo.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
public class JwtAuthConfig extends OncePerRequestFilter {

    private final JwtService jwtService ;
    private final MyUserDetailsService myUserDetailsService ;

    @Autowired
    public JwtAuthConfig(JwtService jwtService, MyUserDetailsService myUserDetailsService) {
        this.jwtService = jwtService;
        this.myUserDetailsService = myUserDetailsService;
    }

    @Override
    protected void doFilterInternal(
           @NonNull HttpServletRequest request,
           @NonNull HttpServletResponse response,
           @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if(authHeader == null && !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);
        final String username = jwtService.extractUsername(token);

        if(username != null  && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails myUser = myUserDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authToke  = new UsernamePasswordAuthenticationToken(
                    myUser,
                    null,
                    myUser.getAuthorities()
            );
        }

    }



}
