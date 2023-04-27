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
@Table(name = "produits_commandes")
@IdClass(ProduitCommanderId.class)
public class ProduitCommander {
    @Id
    @ManyToOne
    @JoinColumn(name = "commande_id")
    @JsonIgnore
    private Commande commande;

    @Id
    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;

    private Integer quantite;

    public String toJson() {

        return "{\"produit\": " + produit.toJson() + ", \"quantite\": " + quantite +"}";
    }
}

