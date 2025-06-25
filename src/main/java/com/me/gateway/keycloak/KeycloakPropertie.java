package com.me.gateway.keycloak;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "keycloak")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KeycloakPropertie {
    private String authServerUrl;
    private String realm;
    private String resource;
    private Admin admin;

    @Getter
    @Setter
    public static class Admin {
        private String username;
        private String password;
    }

}
