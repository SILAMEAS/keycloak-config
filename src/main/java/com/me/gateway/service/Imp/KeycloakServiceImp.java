package com.me.gateway.service.Imp;

import com.me.gateway.dto.UserRegistrationDto;
import com.me.gateway.keycloak.KeycloakPropertie;
import com.me.gateway.service.KeycloakService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.security.KeyException;
import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class KeycloakServiceImp implements KeycloakService {
    private final Keycloak keycloak;
    private final KeycloakPropertie keycloakPropertie;

    @Override
    public Response registerUser(UserRegistrationDto userDto) throws KeyException {
        try{
            UsersResource usersResource =keycloak.realm(keycloakPropertie.getRealm()).users();
            RolesResource rolesResource = keycloak.realm(keycloakPropertie.getRealm()).roles();

            UserRepresentation user = createUserRepresentation(userDto);

            Response response = usersResource.create(user);

            if(response.getStatus()==201){
                String userId = getUserIdFromResponse(response);
                ensureUserRoleExists(rolesResource);
                assignRoleToUser(userId,"user");
                return response;
            }
        }catch (Exception e){
           throw new KeyException("creating error "+e.getMessage(),e);
        }
        return null;
    }

    @Override
    public void deleteUser(String userId) throws KeyException {
        try{
            keycloak.realm(keycloakPropertie.getRealm()).users().get(userId).remove();
        }catch (Exception e){
            throw new KeyException("Error deleting user : "+e.getMessage(),e);
        }

    }

    @Override
    public void updateUser(String userId, UserRegistrationDto userDto) throws KeyException {
        try{
            UsersResource usersResource = keycloak.realm(keycloakPropertie.getRealm()).users();
            UserRepresentation user = createUserRepresentation(userDto);
            usersResource.get(userId).update(user);
        }catch (Exception e){
            throw new KeyException("Error updating user : "+e.getMessage(),e);
        }

    }

    @Override
    public void assignRoleToUser(String userId, String roleName) throws KeyException {
        try{
            UsersResource usersResource = keycloak.realm(keycloakPropertie.getRealm()).users();
            RolesResource rolesResource = keycloak.realm(keycloakPropertie.getRealm()).roles();

            RoleRepresentation role = rolesResource.get(roleName).toRepresentation();
            usersResource.get(userId).roles().realmLevel().add(Collections.singletonList(role));

        }catch (Exception e){
            throw new KeyException("Error assigning roles "+e.getMessage(),e);
        }

    }

    private UserRepresentation createUserRepresentation(UserRegistrationDto userDto){
        UserRepresentation user =new UserRepresentation();
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

    private String getUserIdFromResponse(Response response) {
//      e.g., /auth/admin/realms/{realm}/users/{userId}
        String path = response.getLocation().getPath();
//      returns only the {userId}
        return path.substring(path.lastIndexOf('/') + 1);
    }
    private void ensureUserRoleExists(RolesResource rolesResource){
        try{
            rolesResource.get("user").toRepresentation();
        }catch (Exception e){
            RoleRepresentation userRole = new RoleRepresentation();
            userRole.setName("user");
            userRole.setDescription("Regular user role");
            rolesResource.create(userRole);
        }
    }
}
