package com.example.quickdinner.utils;

import com.example.quickdinner.model.Utilisateur;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class Jwt {
    private static final long expirationTimeInMs = 86400000;
    private static final String secretKey = "SUPERSECRET_KEY_APPP_ZEUBI";

    public static Utilisateur getUserFromToken(String token) {
        try {
            token = token.split(" ")[1];

            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            String userJson = claims.getSubject();

            return Utilisateur.fromJson(userJson);
        } catch (Exception e) {
            return null;
        }
    }
    public static String generate(Utilisateur user) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationTimeInMs);

        return Jwts.builder()
                .setSubject(user.toString())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
}
