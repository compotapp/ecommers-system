package com.ecommers.serviceuser.security;

import com.ecommers.serviceuser.entity.User;
import com.ecommers.serviceuser.util.BaseAuthenticationIntegration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@TestInstance(PER_CLASS)
public class RoleTest extends BaseAuthenticationIntegration {

    @Test
    void getUserByEmailByUserShouldReturnForbidden() {
        String url = getBaseUrl() + "/api/user/email/" + userEmail;

        ResponseEntity<User> response = withUserAuth()
                .getForEntity(url, User.class);

        assertEquals(FORBIDDEN, response.getStatusCode());
    }

    @Test
    void deleteUserByIdByUserShouldReturnForbidden() {
        String url = getBaseUrl() + "/api/user/delete/1";

        withUserAuth().delete(url);
    }

    @Test
    void getUserByNameByAdminShouldReturnForbidden() {
        String url = getBaseUrl() + "/api/user/name/" + adminName;

        ResponseEntity<User> response = withAdminAuth()
                .getForEntity(url, User.class);

        assertEquals(OK, response.getStatusCode());
    }

    @Test
    void updateUserByAdminShouldReturnForbidden() {
        String url = getBaseUrl() + "/api/user/update";
        User currentUser = userRepository.findByEmail(userEmail).orElse(null);
        assertNotNull(currentUser);
        User updateUserRequest = new User(currentUser.getId(), "Neo", "neo@email.com", "789");

        withAdminAuth().put(url, updateUserRequest);
    }
}
