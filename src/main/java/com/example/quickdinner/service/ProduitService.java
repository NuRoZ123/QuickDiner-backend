package com.example.quickdinner.service;

import com.example.quickdinner.model.Produit;

import java.util.List;
import java.util.Optional;

public interface ProduitService {
    List<Produit> findAllByCommercant(int idCommercant);
    Optional<Produit> findById(int id);
    void delete(Produit produit);

    Produit save(Produit produit);
}
