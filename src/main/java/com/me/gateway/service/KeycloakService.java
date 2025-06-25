package com.me.gateway.service;

import com.me.gateway.dto.UserRegistrationDto;
import jakarta.ws.rs.core.Response;

public interface KeycloakService {
    Response registerUser(UserRegistrationDto userDto);
    void deleteUser(String userId);
    void updateUser(String userId,UserRegistrationDto userDto);
    void assignRoleToUser(String userId,String role);
}
