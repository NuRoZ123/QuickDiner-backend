package com.example.quickdinner.service.impl;

import com.example.quickdinner.model.Panier;
import com.example.quickdinner.model.Produit;
import com.example.quickdinner.model.ProduitPanier;
import com.example.quickdinner.repository.ProduitPanierRepository;
import com.example.quickdinner.service.ProduitPanierService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

@Service
@Log
public class ProduitPanierServiceImpl implements ProduitPanierService {

    private final ProduitPanierRepository produitPanierRepository;

    ProduitPanierServiceImpl(ProduitPanierRepository produitPanierRepository) {
        this.produitPanierRepository = produitPanierRepository;
    }

    @Override
    public void deleteProduit(ProduitPanier produitPanier) {
        produitPanierRepository.delete(produitPanier.getPanier().getId(), produitPanier.getProduit().getId());
    }

    @Override
    public ProduitPanier save(ProduitPanier produitPanier) {
        return produitPanierRepository.save(produitPanier);
    }

    @Override
    public void deleteAllByPanier(Panier panier) {
        produitPanierRepository.deleteAllByPanier(panier.getId());
    }

    @Override
    public void deleteAllByProduit(Produit produit) {
        produitPanierRepository.deleteAllByProduit(produit.getId());
    }
}
