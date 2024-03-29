package org.fortech.navigation.security.services.impl;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.fortech.navigation.security.jwt.JwtUtils;
import org.fortech.navigation.security.models.*;
import org.fortech.navigation.security.payload.request.LoginRequest;
import org.fortech.navigation.security.payload.request.SignupRequest;
import org.fortech.navigation.security.payload.request.UserDataRequest;
import org.fortech.navigation.security.payload.response.JwtResponse;
import org.fortech.navigation.security.repos.RoleRepo;
import org.fortech.navigation.security.repos.UserRepo;
import org.fortech.navigation.security.services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.log.LogMessage;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Service
public class UserDetailsManagerImpl implements UserDetailsManager{

    @Autowired
    UserRepo userRepo;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Override
    public void createUser(UserDetails userDetails) {
        User user = new User(userDetails.getUsername(), userDetails.getPassword(),
                userDetails.getAuthorities().stream()
                        .map(authority -> new Role(ERole.valueOf(authority.getAuthority())))
                        .collect(Collectors.toSet()),
                userDetails.isAccountNonExpired(),
                userDetails.isAccountNonLocked(),
                userDetails.isCredentialsNonExpired(),
                userDetails.isEnabled());
        userRepo.save(user);
    }

    @Override
    public void updateUser(UserDetails userDetails) {
            String username = userDetails.getUsername();
            User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

            if(!(userDetails.getAuthorities().isEmpty())) {
                Set<Role> roles = userDetails.getAuthorities().stream()
                        .map(authority -> new Role(ERole.valueOf(authority.getAuthority())))
                        .collect(Collectors.toSet());
                user.setRoles(roles);
            }

            user.setAccountNonExpired(userDetails.isAccountNonExpired());
            user.setAccountNonLocked(userDetails.isAccountNonLocked());
            user.setCredentialsNonExpired(userDetails.isCredentialsNonExpired());
            user.setEnabled(userDetails.isEnabled());

            userRepo.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(String username) {
        try {
            userRepo.deleteByUsername(username);
        } catch (UsernameNotFoundException exception) {
            log.error("Username not found");
        }
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) {
            throw new AccessDeniedException("Can't change password as no Authentication object found in context for current user.");
        } else {
            String username = userDetails.getUsername();
            if (this.authenticationManager != null) {
                log.debug(LogMessage.format("Reauthenticating user '%s' for password change request.", username));
                this.authenticationManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(username, oldPassword));
            } else {
                log.debug("No authentication manager set. Password won't be re-checked.");
            }

            log.debug("Changing password for user '" + username + "'");
            User user = userRepo.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepo.save(user);

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, newPassword));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    @Override
    public boolean userExists(String username) {
        return userRepo.existsByUsername(username);
    }

    @Override
    @Transactional
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }

    public JwtResponse getUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponse(userDetails.getUsername(), userDetails.getEmail(), userDetails.getFirstName(), userDetails.getLastName(), userDetails.getPhoneNumber(), roles);
    }

    public List<User> getAll() {
        return userRepo.findAll();
    }

    public JwtResponse loginUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

        return new JwtResponse(jwt, refreshToken.getToken(),
                userDetails.getUsername(), userDetails.getEmail(), userDetails.getFirstName(),
                userDetails.getLastName(), userDetails.getPhoneNumber(), roles);

    }

    public void registerUser(SignupRequest signUpRequest, boolean[] booleanArray){
        if (userRepo.existsByUsername(signUpRequest.getUsername())) {
            log.error("Error: Username is already in use!");
        }
        if (userRepo.existsByEmail(signUpRequest.getEmail())) {
            log.error("Error: Username is already in use!");
        }
        //role user by default
        Set<Role> roles = new HashSet<>();
        Role roleStart = roleRepo.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(roleStart);

        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        UserDetailsImpl userDetails = new UserDetailsImpl(signUpRequest.getUsername(), passwordEncoder.encode(signUpRequest.getPassword()),authorities,
                booleanArray[0], booleanArray[1], booleanArray[2], booleanArray[3]);

        this.createUser(userDetails);

        String username = signUpRequest.getUsername();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        user.setEmail(signUpRequest.getEmail());
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setPhoneNumber(signUpRequest.getPhoneNumber());

        userRepo.save(user);
    }

    public String logoutUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl)  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    public void changeData(UserDataRequest userData) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username = userDetails.getUsername();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        String email = userData.getEmail();
        String firstName = userData.getFirstName();
        String lastName = userData.getLastName();
        String phoneNumber = userData.getPhoneNumber();

        if(email != null)
            user.setEmail(email);
        if(firstName != null)
            user.setFirstName(firstName);
        if(lastName != null)
            user.setLastName(lastName);
        if(phoneNumber != null)
            user.setPhoneNumber(phoneNumber);

        userRepo.save(user);
    }

    public void setRoles(UserDetailsImpl userDetails, Set<String> strRoles) {
        Set<Role> roles = new HashSet<>();

        strRoles.forEach(role -> {
            switch (role) {
                case "admin" -> {
                    Role adminRole = roleRepo.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);
                }
                case "mod" -> {
                    Role modRole = roleRepo.findByName(ERole.ROLE_MODERATOR)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(modRole);
                }
                default -> {
                    Role userRole = roleRepo.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
                }
            }
        });

        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        userDetails.setAuthorities(authorities);
    }
}
