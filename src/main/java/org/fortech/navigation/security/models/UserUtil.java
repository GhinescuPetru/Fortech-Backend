package org.fortech.navigation.security.models;

import org.fortech.navigation.security.payload.response.UserResponse;

public class UserUtil {

    public static UserResponse build(User user) {
        return new UserResponse(user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getRoles());
    }
}
