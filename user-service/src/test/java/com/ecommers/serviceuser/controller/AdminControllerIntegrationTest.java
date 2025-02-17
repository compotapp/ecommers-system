package com.ecommers.serviceuser.controller;

import com.ecommers.serviceuser.entity.User;
import com.ecommers.serviceuser.util.BaseAuthenticationIntegration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class AdminControllerIntegrationTest extends BaseAuthenticationIntegration {

    @Test
    void shouldRegisterUser() {
        var name = "Test";
        var email = "test@mail.com";
        var password = "123";
        var userRegisterRequest = new User(name, email, password);

        ResponseEntity<User> response = restTemplate.postForEntity(
                "/api/user/register",
                userRegisterRequest,
                User.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        var result = response.getBody();
        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(email, result.getEmail());
        assertEquals(password, result.getPassword());
        assertNotNull(result.getId());
    }

    @Test
    void shouldFoundUserByName() {
        String url = getBaseUrl() + "/api/user/" + adminName;

        ResponseEntity<User> response =
                withAdminAuth().getForEntity(url, User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        var user = response.getBody();

        assertNotNull(user);
        assertEquals(adminName, user.getName());
    }

    @Test
    void shouldFoundUserByNameIfNameNotFoundInDatabase() {
        String url = "http://localhost:" + port + "/api/user/Bob";

        ResponseEntity<User> response = restTemplate.getForEntity(url, User.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void shouldFoundUserByEmail() {
        String url = "http://localhost:" + port + "/api/user/max@email.com";

        ResponseEntity<User> response = restTemplate.getForEntity(url, User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        var user = response.getBody();

        assertNotNull(user);
        assertEquals("max@email.com", user.getEmail());
    }

    @Test
    void shouldFoundUserByEmailIfEmailNotFoundInDatabase() {
        String url = "http://localhost:" + port + "/api/user/bob@email.com";

        ResponseEntity<User> response = restTemplate.getForEntity(url, User.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void shouldCorrectDeleteAllUserAndCreateUser() {
        List<User> users = userRepository.findAll();

        assertEquals(2, users.size());

        userRepository.deleteAll();
        users = userRepository.findAll();

        assertEquals(0, users.size());

        User user = new User("Neo", "neo@email.com", "789");
        String url = "http://localhost:" + port + "/api/user/register";
        ResponseEntity<User> response = restTemplate.postForEntity(
                url,
                user,
                User.class
        );
        users = userRepository.findAll();
        User userResponse = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(userResponse);
        assertEquals(1, users.size());
        assertEquals(user.getName(), userResponse.getName());
        assertEquals(user.getEmail(), userResponse.getEmail());
        assertEquals(user.getEmail(), userResponse.getEmail());
        assertNotNull(userResponse.getId());
    }

    @Test
    void shouldUpdateUser() {
        User currentUser = userRepository.findByEmail("max@email.com").orElse(null);

        assertNotNull(currentUser);

        User updateUserRequest = new User(currentUser.getId(), "Neo", "neo@email.com", "789");
        String url = "http://localhost:" + port + "/api/user/update";
        restTemplate.put(
                url,
                updateUserRequest,
                User.class
        );

        User updateUser = userRepository.findById(currentUser.getId()).orElse(null);

        assertNotNull(updateUser);
        assertEquals(updateUserRequest.getId(), updateUser.getId());
        assertEquals(updateUserRequest.getName(), updateUser.getName());
        assertEquals(updateUserRequest.getEmail(), updateUser.getEmail());
        assertEquals(updateUserRequest.getPassword(), updateUser.getPassword());
    }

    @Test
    void shouldUpdateNotExistUserAndResponseNotFound() {
        User updateUserRequest = new User(100L, "Neo", "neo@email.com", "789");
        String url = "http://localhost:" + port + "/api/user/update";
        restTemplate.put(
                url,
                updateUserRequest,
                User.class
        );
        User updateUser = userRepository.findByEmail("neo@email.com").orElse(null);

        assertNull(updateUser);
    }

    @Test
    void shouldDeleteUser() {
        User currentUser = userRepository.findByEmail("max@email.com").orElse(null);

        assertNotNull(currentUser);

        String url = "http://localhost:" + port + "/api/user/delete/" + currentUser.getId();
        restTemplate.delete(url);
        User deleteUser = userRepository.findByEmail("max@email.com").orElse(null);

        assertNull(deleteUser);
    }
}