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

@Table(name = "Produits")
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    private String description;
    private String image;
    private Double prix;

    @ManyToMany(targetEntity = Commercant.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Commercant> commercants;
}
