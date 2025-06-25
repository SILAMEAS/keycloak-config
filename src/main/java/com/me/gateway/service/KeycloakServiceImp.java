package com.me.gateway.service;

import com.me.gateway.dto.UserRegistrationDto;
import com.me.gateway.keycloak.KeycloakPropertie;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeycloakServiceImp implements KeycloakService {
    private final Keycloak keycloak;
    private final KeycloakPropertie keycloakPropertie;

    @Override
    public Response registerUser(UserRegistrationDto userDto) {
        try{
            UsersResource usersResource =keycloak.realm(keycloakPropertie.getRealm()).users();
            RolesResource rolesResource = keycloak.realm(keycloakPropertie.getRealm()).roles();

//            UserRepresentation user = createUserRepresentation(userDto);
//
//            Response reponse = usersResource.create(user);
//
//            if(reponse.getStatus()==201){
//                String userId = getUserIdFromResponse(reponse);
//                ensureUserRoleExists(rolesResource);
//                ass
//            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteUser(String userId) {

    }

    @Override
    public void updateUser(String userId, UserRegistrationDto userDto) {

    }

    @Override
    public void assignRoleToUser(String userId, String role) {

    }
}
