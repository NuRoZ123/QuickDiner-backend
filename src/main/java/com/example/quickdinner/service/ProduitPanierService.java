package com.example.quickdinner.service;

import com.example.quickdinner.model.Panier;
import com.example.quickdinner.model.Produit;
import com.example.quickdinner.model.ProduitPanier;

public interface ProduitPanierService {

    void deleteProduit(ProduitPanier produitPanier);
    ProduitPanier save(ProduitPanier produitPanier);
    void deleteAllByPanier(Panier panier);
    void deleteAllByProduit(Produit produit);
}
