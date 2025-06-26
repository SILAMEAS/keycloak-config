package com.me.gateway.service.impl;

import com.me.gateway.dto.UserRegistrationDto;
import com.me.gateway.keycloak.KeycloakPropertie;
import com.me.gateway.service.KeycloakService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.security.KeyException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakServiceImp implements KeycloakService {

    private final Keycloak keycloak;
    private final KeycloakPropertie keycloakPropertie;

    @Override
    public Response registerUser(UserRegistrationDto userDto) throws KeyException {
        try {
            RealmResource realm = keycloak.realm(keycloakPropertie.getRealm());
            UsersResource usersResource = realm.users();
            RolesResource rolesResource = realm.roles();

            UserRepresentation user = createUserRepresentation(userDto);
            Response response = usersResource.create(user);

            if (response.getStatus() == 201) {
                String userId = extractUserId(response);
                ensureUserRoleExists(rolesResource);
                assignRoleToUser(userId, "user");
                log.info("User '{}' registered with ID {}", userDto.getUsername(), userId);
                return response;
            } else if (response.getStatus() == 409) {
                throw new KeyException("User already exists");
            } else {
                throw new KeyException("Unexpected response status: " + response.getStatus());
            }

        } catch (Exception e) {
            log.error("Error registering user '{}': {}", userDto.getUsername(), e.getMessage(), e);
            throw new KeyException("Error registering user", e);
        }
    }

    @Override
    public void deleteUser(String userId) throws KeyException {
        try {
            RealmResource realm = keycloak.realm(keycloakPropertie.getRealm());
            realm.users().get(userId).remove();
            log.info("User with ID '{}' deleted", userId);
        } catch (Exception e) {
            log.error("Error deleting user '{}': {}", userId, e.getMessage(), e);
            throw new KeyException("Error deleting user", e);
        }
    }

    @Override
    public void updateUser(String userId, UserRegistrationDto userDto) throws KeyException {
        try {
            RealmResource realm = keycloak.realm(keycloakPropertie.getRealm());
            UsersResource usersResource = realm.users();
            UserRepresentation user = createUserRepresentation(userDto);
            usersResource.get(userId).update(user);
            log.info("User with ID '{}' updated", userId);
        } catch (Exception e) {
            log.error("Error updating user '{}': {}", userId, e.getMessage(), e);
            throw new KeyException("Error updating user", e);
        }
    }

    @Override
    public void assignRoleToUser(String userId, String roleName) throws KeyException {
        try {
            RealmResource realm = keycloak.realm(keycloakPropertie.getRealm());
            UsersResource usersResource = realm.users();
            RolesResource rolesResource = realm.roles();

            RoleRepresentation role = rolesResource.get(roleName).toRepresentation();
            usersResource.get(userId).roles().realmLevel().add(Collections.singletonList(role));
            log.info("Assigned role '{}' to user ID '{}'", roleName, userId);
        } catch (Exception e) {
            log.error("Error assigning role '{}' to user '{}': {}", roleName, userId, e.getMessage(), e);
            throw new KeyException("Error assigning role to user", e);
        }
    }

    @Override
    public List<UserRepresentation> getAllUsers() throws Exception {
        try {
            RealmResource realm = keycloak.realm(keycloakPropertie.getRealm());
            return realm.users().list();
        } catch (Exception e) {
            log.error("Error fetching users: {}", e.getMessage(), e);
            throw new Exception("Failed to retrieve users", e);
        }
    }


    private UserRepresentation createUserRepresentation(UserRegistrationDto userDto) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmailVerified(false);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(userDto.getPassword());
        credential.setTemporary(false);
        user.setCredentials(Collections.singletonList(credential));

        return user;
    }

    private String extractUserId(Response response) throws KeyException {
        URI location = response.getLocation();
        if (location == null) {
            throw new KeyException("Missing 'Location' header in response");
        }
        String path = location.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    private void ensureUserRoleExists(RolesResource rolesResource) {
        try {
            rolesResource.get("user").toRepresentation();
        } catch (Exception e) {
            log.warn("Role 'user' not found, creating one");
            RoleRepresentation userRole = new RoleRepresentation();
            userRole.setName("user");
            userRole.setDescription("Default user role");
            rolesResource.create(userRole);
        }
    }
}
