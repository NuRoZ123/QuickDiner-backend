package com.example.quickdinner.model;

import jakarta.persistence.*;
import lombok.*;
import org.json.JSONObject;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity

@Table(name = "Utilisateurs")
public class Utilisateur {
    public static Utilisateur fromJson(String json) {
        JSONObject obj = new JSONObject(json);

        String nom = obj.getString("nom");
        String prenom = obj.getString("prenom");
        String email = obj.getString("email");

        return Utilisateur.builder()
                .nom(nom)
                .prenom(prenom)
                .email(email)
                .build();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    private String prenom;
    private String email;
    private String password;

    @Override
    public String toString() {
        return String.format("{\"nom\": \"%s\", \"prenom\": \"%s\", \"email\": \"%s\"}", nom, prenom, email);
    }
}
