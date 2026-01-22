package com.expense.ExpenseTrackerApplication.JWT;


import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Slf4j
@Service
public class JwtService {

    private static final String SECRET_KEY = "my-secret-key-that-is-long-enough-at-least-32-bytes";

    private Key getSigningKey() {
        log.info("inside getSigningKey");
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
/*
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }*/
    public String generateToken(UserDetails userDetails) {
        log.info("Generate the Token for use {}:", userDetails.getUsername());// Why this print emailId, It should print the userName.
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(getSigningKey())  // ✅ No more SignatureAlgorithm enum
                .compact();
    }

    public String extractUsername(String token) {
        log.info("Inside extractUsername");
        log.info("Extracting the Token {}:-",token);
        return getParser()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        log.info("Inside isTokenValid");
        String username = extractUsername(token);
        log.info("userName {}:", username);
        return username.equals(userDetails.getUsername());
    }

    private JwtParser getParser() {
        log.info("getParser");
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build();
    }
}
