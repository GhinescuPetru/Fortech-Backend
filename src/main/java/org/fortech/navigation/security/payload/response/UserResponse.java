package org.fortech.navigation.security.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.fortech.navigation.security.models.Role;

import java.util.Set;

@Data
@AllArgsConstructor
public class UserResponse {

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private Set<Role> roles;
}
