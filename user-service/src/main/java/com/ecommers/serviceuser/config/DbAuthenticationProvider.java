package com.ecommers.serviceuser.config;

import com.ecommers.serviceuser.service.RoleService;
import com.ecommers.serviceuser.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;


@Component
class DbAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;

    DbAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return userService.findByEmail(authentication.getName())
                   .map(user -> new PlainAuthentication(user.getId()))
                   .orElseThrow(() -> new AuthenticationServiceException("Invalid username or password"));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}
