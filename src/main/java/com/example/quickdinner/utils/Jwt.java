package com.example.quickdinner.utils;

import com.example.quickdinner.model.Utilisateur;
import com.example.quickdinner.service.UtilisateurService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.JSONObject;

import java.util.Date;
import java.util.Optional;

public class Jwt {
    private static final long expirationTimeInMs = 86400000;
    private static final String secretKey = "SUPERSECRET_KEY_APPP_ZEUBI";

    public static Optional<Utilisateur> getUserFromToken(String token, UtilisateurService utilisateurService) {
        try {
            String bearer = token.split(" ")[0];
            token = token.split(" ")[1];

            if(!"Bearer".equals(bearer)) {
                return null;
            }

            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            String userJson = claims.getSubject();
            JSONObject obj = new JSONObject(userJson);
            String email = obj.getString("email");

            return utilisateurService.findByEmail(email);
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
