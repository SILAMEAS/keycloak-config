package com.me.gateway.service;

import com.me.gateway.dto.UserRegistrationDto;
import jakarta.ws.rs.core.Response;

import java.security.KeyException;

public interface KeycloakService {
    Response registerUser(UserRegistrationDto userDto) throws KeyException;
    void deleteUser(String userId) throws KeyException;
    void updateUser(String userId,UserRegistrationDto userDto) throws KeyException;
    void assignRoleToUser(String userId,String role) throws KeyException;
}
