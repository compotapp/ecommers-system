package com.ecommers.serviceuser.util;

import com.ecommers.serviceuser.entity.User;
import com.ecommers.serviceuser.entity.UserRole;
import com.ecommers.serviceuser.repository.RoleRepository;
import com.ecommers.serviceuser.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.ecommers.serviceuser.role.RoleType.ADMIN;
import static com.ecommers.serviceuser.role.RoleType.USER;

public class BaseAuthenticationIntegration extends AbstractTestContainer {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected RoleRepository roleRepository;

    @LocalServerPort
    protected int port;

    protected final String adminName = "Admin";
    protected final String userName = "User";
    protected final String adminEmail = "admin@email.com";
    protected final String userEmail = "user@email.com";
    protected final String adminPassword = "111";
    protected final String userPassword = "222";

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        roleRepository.deleteAll();
        userRepository.deleteAll();
        User admin = userRepository.save(new User(adminName, adminEmail, adminPassword));
        User user = userRepository.save(new User(userName, userEmail, userPassword));
        roleRepository.save(new UserRole(admin, ADMIN));
        roleRepository.save(new UserRole(user, USER));
    }

    protected String getBaseUrl() {
        return "http://localhost:" + port;
    }

    protected TestRestTemplate withAdminAuth() {
        return restTemplate.withBasicAuth(adminEmail, adminPassword);
    }

    protected TestRestTemplate withUserAuth() {
        return restTemplate.withBasicAuth(userEmail, userPassword);
    }
}
