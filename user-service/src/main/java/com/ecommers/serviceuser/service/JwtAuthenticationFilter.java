package com.ecommers.serviceuser.service;

import com.ecommers.serviceuser.entity.User;
import com.ecommers.serviceuser.exeption.UserNotFoundException;
import com.ecommers.serviceuser.role.Role;
import com.ecommers.serviceuser.role.RoleType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;
    private final RoleService roleService;

    public JwtAuthenticationFilter(JwtService jwtService, UserService userService, RoleService roleService) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String jwt = authHeader.substring(7);
        String username = jwtService.extractUsername(jwt);
        List<String> roles = jwtService.extractRoles(jwt);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userService.findByName(username).orElseThrow(() -> new UserNotFoundException("User by name: " + username + " not found"));
            if (jwtService.isTokenValid(jwt, user) && roleService.hasAnyRole(RoleType.of(roles).toArray(new Role[0]))) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                roles.stream()
                                        .map(SimpleGrantedAuthority::new)
                                        .collect(Collectors.toList())
                        );
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
