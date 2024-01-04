package org.fortech.navigation.security.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.fortech.navigation.security.models.User;
import org.fortech.navigation.security.models.UserUtil;
import org.fortech.navigation.security.payload.request.UserUpdateRequest;
import org.fortech.navigation.security.models.UserDetailsImpl;
import org.fortech.navigation.security.payload.response.UserResponse;
import org.fortech.navigation.security.services.impl.UserDetailsManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/admin") //Suppress warnings for mapping annotation
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    UserDetailsManagerImpl userDetailsManager;

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        try {
            List<User> users = userDetailsManager.getAll();
            List<UserResponse> userResponse = users.stream().map(UserUtil::build).toList();
            return ResponseEntity.ok(userResponse);
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/users/{username}")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest, @PathVariable String username){
        try {
            UserDetailsImpl userDetails = userDetailsManager.loadUserByUsername(username);
            if(!(userUpdateRequest.getAccountNonExpired() == null)) {
                userDetails.setAccountNonExpired(userUpdateRequest.getAccountNonExpired());
            }
            if(!(userUpdateRequest.getAccountNonLocked() == null)) {
                userDetails.setAccountNonLocked(userUpdateRequest.getAccountNonLocked());
            }
            if(!(userUpdateRequest.getCredentialsNonExpired() == null)) {
                userDetails.setCredentialsNonExpired(userUpdateRequest.getCredentialsNonExpired());
            }
            if(!(userUpdateRequest.getEnabled() == null)) {
                userDetails.setEnabled(userUpdateRequest.getEnabled());
            }

            userDetailsManager.setRoles(userDetails, userUpdateRequest.getRoles());
            userDetailsManager.updateUser(userDetails);
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("User updated successfully!");
    }

    @DeleteMapping("/users/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        try {
            userDetailsManager.deleteUser(username);
        } catch (UsernameNotFoundException exception) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("User deleted successfully!");
    }
}
