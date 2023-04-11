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

@Table(name = "Paniers")
public class Panier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToMany(targetEntity = Produit.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Produit> produits;
}
