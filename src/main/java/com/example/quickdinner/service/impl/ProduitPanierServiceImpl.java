package com.example.quickdinner.service.impl;

import com.example.quickdinner.model.Panier;
import com.example.quickdinner.model.ProduitPanier;
import com.example.quickdinner.repository.ProduitPanierRepository;
import com.example.quickdinner.service.ProduitPanierService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@Log
public class ProduitPanierServiceImpl implements ProduitPanierService {

    @PersistenceContext
    private EntityManager entityManager;

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
}
