package com.la.keycloak.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto {
    @NotBlank(message = "Username is need")
    @Size(min = 3,max = 50,message = "Username must be 3 to 50")
    String username;

    @NotBlank(message = "email is need")
            @Email(message = "invalid email")
    String email;

    @NotBlank(message = "password is need")
    @Size(min = 3,max = 50,message = "password must be 3 to 50")
    String password;
    @NotBlank(message = "firstName is need")
    @Size(min = 3,max = 50,message = "firstName must be 3 to 50")
    String firstName;
    @NotBlank(message = "lastName is need")
    @Size(min = 3,max = 50,message = "lastName must be 3 to 50")
    String lastName;
}
