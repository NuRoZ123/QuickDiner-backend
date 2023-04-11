package com.example.quickdinner.model;

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

    @Column(name= "image", columnDefinition = "LONGTEXT")
    private String image;

//    @ManyToMany(targetEntity = Produit.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "commercants")
//    private List<Produit> produits;
}
