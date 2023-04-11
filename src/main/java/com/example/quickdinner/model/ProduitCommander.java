package com.example.quickdinner.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

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
    private Commande commande;

    @Id
    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;

    private Integer quantite;
}

