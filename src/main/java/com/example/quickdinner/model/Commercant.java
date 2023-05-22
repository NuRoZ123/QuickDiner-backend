package com.example.quickdinner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity

@Table(name = "Commercants")
public class Commercant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    private String adresse;
    private String ville;
    private Double latitude;
    private Double longitude;

    @Column(name= "image", columnDefinition = "LONGTEXT")
    private String image;

    @JsonIgnore
    @OneToOne
    private Utilisateur manager;

    @JsonIgnore
    @OneToMany(mappedBy = "commercant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Produit> produits;

    @JsonIgnore
    @OneToMany(mappedBy = "commercant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CommentaireCommercants> commentaires;
}
