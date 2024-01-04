package org.fortech.navigation.security.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.fortech.navigation.security.payload.request.PasswordRequest;
import org.fortech.navigation.security.payload.request.UserDataRequest;
import org.fortech.navigation.security.models.UserDetailsImpl;
import org.fortech.navigation.security.services.impl.UserDetailsManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/profile") //Suppress warnings for mapping annotation
@SecurityRequirement(name = "bearerAuth")
public class UserProfileController {

    @Autowired
    UserDetailsManagerImpl userDetailsManager;

    @GetMapping
    public ResponseEntity<?> getUser() {
       try {
           return ResponseEntity.ok(userDetailsManager.getUser());
       } catch (UsernameNotFoundException exception) {
           return ResponseEntity.notFound().build();
       }
    }

    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordRequest passwordRequest) {
        try {
            userDetailsManager.changePassword(passwordRequest.getOldPassword(), passwordRequest.getNewPassword());
        } catch(AccessDeniedException ex) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok("Password changed successfully!");
    }

    @PutMapping("/data")
    public ResponseEntity<?> changeUserData(@Valid @RequestBody UserDataRequest userData) {
        try {
            userDetailsManager.changeData(userData);
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Data changed successfully!");
    }
    @DeleteMapping
    public ResponseEntity<?> deleteUser() {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userDetailsManager.deleteUser(userDetails.getUsername());
        } catch (UsernameNotFoundException exception) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("User deleted successfully!");
    }

}
