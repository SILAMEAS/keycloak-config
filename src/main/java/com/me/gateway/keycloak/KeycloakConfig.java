package com.me.gateway.keycloak;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {
    private final KeycloakPropertie keycloakPropertie;
    public KeycloakConfig(KeycloakPropertie keycloakPropertie) {
        this.keycloakPropertie = keycloakPropertie;
    }
    @Bean
    public Keycloak keycloak(){
        return KeycloakBuilder.builder()
                .serverUrl(keycloakPropertie.getAuthServerUrl())
                .realm("master")
                .clientId(keycloakPropertie.getResource())
                .username(keycloakPropertie.getAdmin().getUsername())
                .password(keycloakPropertie.getAdmin().getPassword())
                .grantType(OAuth2Constants.PASSWORD)
                .build();

    }
}
