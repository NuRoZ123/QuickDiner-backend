package com.example.quickdinner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "paniers_produits")
@IdClass(ProduitPanierId.class)
public class ProduitPanier {
    @Id
    @ManyToOne
    @JoinColumn(name = "panier_id")
    @JsonIgnore
    private Panier panier;

    @Id
    @ManyToOne
    @JoinColumn(name = "produits_id")
    private Produit produit;

    private Integer quantite;
}

