package com.example.quickdinner.service;

import com.example.quickdinner.model.Produit;

import java.util.List;

public interface ProduitService {
    List<Produit> findAllByCommercant(int idCommercant);
}
