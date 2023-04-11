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

@Table(name = "Produits")
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    private String description;
    private Float prix;

    @ManyToOne(targetEntity = Commercant.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JsonIgnore
    private Commercant commercant;
}
