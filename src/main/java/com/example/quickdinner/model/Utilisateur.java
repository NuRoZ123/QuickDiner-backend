package com.example.quickdinner.model;

import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;

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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "Identifiant unique de l'utilisateur", example = "1")
    private Integer id;
    private String nom;
    private String prenom;
    private String email;
    private String password;

    @ManyToOne
    private Role role;

    @Override
    public String toString() {
        return String.format("{\"nom\": \"%s\", \"prenom\": \"%s\", \"email\": \"%s\", \"role\": \"%s\"}",
                nom, prenom, email, role.getLibelle());
    }
}
