package com.me.gateway.controller;

import com.me.gateway.dto.UserRegistrationDto;
import com.me.gateway.service.KeycloakService;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/public")
@RequiredArgsConstructor
public class Controller {
    private final KeycloakService keycloakService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto){
        try {
            Response response = keycloakService.registerUser(userRegistrationDto);
            if(response.getStatus()==201){
                return ResponseEntity.ok("User created Successfully");
            }else {
                String errorMessage = response.readEntity(String.class);
                return ResponseEntity.badRequest().body("Failed to register user: "+errorMessage);
            }
        }catch (Exception e){
           return ResponseEntity.badRequest().body("Failed to register user: "+e.getMessage());
        }
    }

    @GetMapping
    public String publicMethod() {
        return "publicMethod";
    }

    @GetMapping("/heath")
    public String heath() {
        return "heath";
    }

}
