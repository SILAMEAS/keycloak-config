package com.la.keycloak.controller;

import com.la.keycloak.dto.UserRegistrationDto;
import com.la.keycloak.service.KeycloakService;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/public")
@RequiredArgsConstructor
@Slf4j
public class Controller {

    private final KeycloakService keycloakService;
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserRepresentation> users = keycloakService.getAllUsers();
            log.info("Fetched {} users", users.size());
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("Error retrieving users: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch users: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationDto userDto) {
        try {
            Response response = keycloakService.registerUser(userDto);
            int status = response.getStatus();

            if (status == 201) {
                log.info("User '{}' registered successfully", userDto.getUsername());
                return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
            }

            String errorMessage = tryReadErrorMessage(response);
            log.warn("User '{}' registration failed: {}", userDto.getUsername(), errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to register user: " + errorMessage);

        } catch (Exception e) {
            log.error("Exception during user registration: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal error: " + e.getMessage());
        }
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId) {
        try {
            keycloakService.deleteUser(userId);
            log.info("User with ID '{}' deleted successfully", userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting user '{}': {}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete user: " + e.getMessage());
        }
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<String> updateUser(
            @PathVariable String userId,
            @Valid @RequestBody UserRegistrationDto userDto
    ) {
        try {
            keycloakService.updateUser(userId, userDto);
            log.info("User with ID '{}' updated successfully", userId);
            return ResponseEntity.ok("User updated successfully");
        } catch (Exception e) {
            log.error("Error updating user '{}': {}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update user: " + e.getMessage());
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Service is healthy");
    }

    @GetMapping("/ping")
    public ResponseEntity<String> publicPing() {
        return ResponseEntity.ok("Public endpoint is reachable");
    }

    private String tryReadErrorMessage(Response response) {
        try {
            return response.readEntity(String.class);
        } catch (Exception e) {
            return "Unknown error";
        }
    }

}
