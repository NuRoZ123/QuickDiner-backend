package com.example.quickdinner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

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
    private Integer id;
    private String nom;
    private String prenom;
    private String email;

    @JsonIgnore
    private String password;
    @ManyToOne
    @JsonIgnore
    private Role role;

    @ManyToOne
    @JsonIgnore
    private Panier panier;

    @Override
    public String toString() {
        return String.format("{\"nom\": \"%s\", \"prenom\": \"%s\", \"email\": \"%s\", \"role\": \"%s\"}",
                nom, prenom, email, role.getLibelle());
    }
}
