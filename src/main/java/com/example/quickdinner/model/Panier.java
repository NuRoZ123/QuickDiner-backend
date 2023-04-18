package com.example.quickdinner.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity

@Table(name = "Paniers")
public class Panier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "panier", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ProduitPanier> produitPaniers;

    public void addProduit(ProduitPanier produitPanier) {
        this.produitPaniers.add(produitPanier);
    }

    public void removeProduit(Produit produit) {
        this.produitPaniers = this.produitPaniers.stream()
                .filter(produitPanier ->
                        !Objects.equals(produitPanier.getProduit().getId(), produit.getId()))
                .collect(Collectors.toList());
    }

    public Optional<ProduitPanier> getProduitsToProduitPanier(Produit produit) {
        return this.produitPaniers.stream()
                .filter(produitPanier ->
                        Objects.equals(produitPanier.getProduit().getId(), produit.getId()))
                .findFirst();
    }

    public boolean hasProduit(Produit produit) {
        return this.produitPaniers.stream()
                .anyMatch(produitPanier ->
                        Objects.equals(produitPanier.getProduit().getId(), produit.getId()));
    }
}
