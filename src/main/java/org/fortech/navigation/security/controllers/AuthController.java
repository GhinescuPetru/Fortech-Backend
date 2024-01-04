package org.fortech.navigation.security.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.fortech.navigation.security.exception.TokenRefreshException;
import org.fortech.navigation.security.services.RefreshTokenService;
import org.fortech.navigation.security.payload.request.LoginRequest;
import org.fortech.navigation.security.payload.request.SignupRequest;
import org.fortech.navigation.security.services.impl.UserDetailsManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth") //Suppress warnings for mapping annotation
@SecurityRequirement(name = "bearerAuth")
public class AuthController {

    @Autowired
    UserDetailsManagerImpl userDetailsManager;

    @Autowired
    RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            return ResponseEntity.ok(userDetailsManager.loginUser(loginRequest));
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        boolean[] booleanArray = new boolean[4];
        Arrays.fill(booleanArray, Boolean.TRUE); //harcoded verifications
        try{
            userDetailsManager.registerUser(signUpRequest, booleanArray);
        } catch (RuntimeException exception) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody @NotBlank String refreshToken) {
        try {
            return refreshTokenService.createAuthToken(refreshToken);
        } catch (TokenRefreshException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        try {
            String username = userDetailsManager.logoutUser();
            refreshTokenService.deleteByUsername(username);
        } catch(UsernameNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Log out successful!");
    }

}