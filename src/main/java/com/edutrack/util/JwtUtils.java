package com.edutrack.util;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.apache.commons.lang3.time.DateUtils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.edutrack.entities.enums.UserType;

@Component
@Slf4j
public class JwtUtils {
    // Clave secreta para verificar el token
    private static final SecretKey secretKey = Jwts.SIG.HS256.key().build();

    private static final String ISSUER = "server";

    private JwtUtils(){}

    public static boolean validateToken (String jwtToken){
        return parseToken(jwtToken).isPresent();
    }

    public static Optional<Claims> parseToken(String jwtToken){
    var jwtParser = Jwts.parser()
                    .verifyWith(secretKey)
                    .build();

    try {
        return Optional.of(jwtParser.parseSignedClaims(jwtToken).getPayload());
    } catch (JwtException | IllegalArgumentException e) {
        System.err.print("JWT Exception ocurred: " + e.getMessage());
    }
        return Optional.empty();
    }

    public static Optional<String> getUsernameFromToken(String jwtToken){
        var claimsOptional = parseToken(jwtToken);
        return claimsOptional.map(Claims::getSubject);
    }

    public static String generateToken (String username, UserType role, Long institutionId){
        var currentDate = new Date();
        var jwtExpirationInMinutes = 10;
        var expiration = DateUtils.addMinutes(currentDate, jwtExpirationInMinutes);

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuer(ISSUER)
                .subject(username)
                .claim("role", role)
                .claim("institutionId", institutionId)
                .signWith(secretKey)
                .issuedAt(currentDate)
                .expiration(expiration)
                .compact();
    }

    public static Long extractInstitutionId(String token) {
        try {
            Optional<Claims> claimsOptional = parseToken(token);
            if (claimsOptional.isPresent()) {
                return claimsOptional.get().get("institutionId", Long.class);
            }
        } catch (Exception e) {
            log.error("Error al extraer institutionId del token", e);
        }
        return null;
    }

}


