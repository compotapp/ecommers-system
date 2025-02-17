package com.ecommers.serviceuser.controller;

import com.ecommers.serviceuser.entity.User;
import com.ecommers.serviceuser.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("@RoleService.hasAnyRole(@Role.ADMIN)")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("@RoleService.hasAnyRole(@Role.ADMIN)")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Validated @RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("@RoleService.hasAnyRole(@Role.USER)")
    public ResponseEntity<User> getUserByName(@PathVariable String name) {
        return userService.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update")
    @PreAuthorize("@RoleService.hasAnyRole(@Role.USER)")
    public ResponseEntity<User> updateUser(@Validated @RequestBody User user) {
        return userService.updateUser(user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
