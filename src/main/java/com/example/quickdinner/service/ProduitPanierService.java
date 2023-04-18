package com.example.quickdinner.service;

import com.example.quickdinner.model.ProduitPanier;

public interface ProduitPanierService {

    void deleteProduit(ProduitPanier produitPanier);

    ProduitPanier save(ProduitPanier produitPanier);
}
