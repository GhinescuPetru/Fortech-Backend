package org.fortech.navigation.security.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.fortech.navigation.security.exception.TokenRefreshException;
import org.fortech.navigation.security.jwt.JwtUtils;
import org.fortech.navigation.security.models.RefreshToken;
import org.fortech.navigation.security.payload.response.TokenRefreshResponse;
import org.fortech.navigation.security.repos.RefreshTokenRepo;
import org.fortech.navigation.security.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class RefreshTokenService {
    @Value("${org.fortech.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    JwtUtils jwtUtils;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepo.findByToken(token);
    }

    public RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepo.findByUsername(username).orElse(null));
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        return refreshTokenRepo.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepo.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    @Transactional
    public void deleteByUsername(String username) {
        refreshTokenRepo.deleteByUser(userRepo.findByUsername(username).orElse(null));
    }

    public ResponseEntity<?> createAuthToken(String refreshToken) {
        return this.findByToken(refreshToken)
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, refreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(refreshToken,
                        "Refresh token is not in database!"));
    }
}