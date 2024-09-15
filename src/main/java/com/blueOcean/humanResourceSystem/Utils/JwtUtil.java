package com.blueOcean.humanResourceSystem.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    // Inject the secret key from application.yml
    @Value("${jwt.secret-key}")
    private String secretKey;
    private static final long EXPIRATION_TIME = 1000*60*60; // 1 day in milliseconds

    public String generateToken(String username) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + EXPIRATION_TIME);

        return JWT.create()
                .withClaim("username", username) // Set username as "username" claim
                .withIssuedAt(now)
                .withExpiresAt(exp)
                .sign(Algorithm.HMAC256(secretKey));
    }

    // Validates the JWT token
    public boolean validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false; // Token is invalid or expired
        }
    }

    // Extracts the username from the JWT token
    public String getUsernameFromToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaim("username").asString();
    }
}
