package com.example.quickdinner.service.impl;

import com.example.quickdinner.model.Commande;
import com.example.quickdinner.model.Produit;
import com.example.quickdinner.model.ProduitCommander;
import com.example.quickdinner.repository.ProduitCommanderRepository;
import com.example.quickdinner.service.ProduitCommanderService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
public class ProduitCommanderServiceImpl implements ProduitCommanderService {

    private final ProduitCommanderRepository produitCommanderRepository;

    ProduitCommanderServiceImpl(ProduitCommanderRepository produitCommanderRepository) {
        this.produitCommanderRepository = produitCommanderRepository;
    }

    @Override
    public List<ProduitCommander> findAllByProduitId(int idProduit) {
        return produitCommanderRepository.findByProduitId(idProduit);
    }

    @Override
    public List<ProduitCommander> findAllByCommandeId(int idCommande) {
        return produitCommanderRepository.findByCommandeId(idCommande);
    }

    @Override
    public ProduitCommander save(ProduitCommander produitCommander) {
        return produitCommanderRepository.save(produitCommander);
    }

    @Override
    public List<ProduitCommander> saveAll(List<ProduitCommander> produitCommanders) {
        return produitCommanderRepository.saveAll(produitCommanders);
    }

    @Override
    public void deleteAllByCommande(Commande commande) {
        produitCommanderRepository.deleteAllByCommande(commande.getId());
    }

    @Override
    public void deleteAllByProduit(Produit produit) {
        produitCommanderRepository.deleteAllByProduit(produit.getId());
    }
}
