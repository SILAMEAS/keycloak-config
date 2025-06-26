package com.me.gateway.service;

import com.me.gateway.dto.UserRegistrationDto;
import jakarta.ws.rs.core.Response;
import org.keycloak.representations.idm.UserRepresentation;

import java.security.KeyException;
import java.util.List;

public interface KeycloakService {
    Response registerUser(UserRegistrationDto userDto) throws KeyException;
    void deleteUser(String userId) throws KeyException;
    void updateUser(String userId,UserRegistrationDto userDto) throws KeyException;
    void assignRoleToUser(String userId,String role) throws KeyException;
    List<UserRepresentation> getAllUsers() throws Exception;
}
