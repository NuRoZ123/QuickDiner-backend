package com.example.quickdinner.service;

import com.example.quickdinner.model.Commande;
import com.example.quickdinner.model.Produit;
import com.example.quickdinner.model.ProduitCommander;

import java.util.List;

public interface ProduitCommanderService {
    List<ProduitCommander> findAllByProduitId(int idProduit);
    List<ProduitCommander> findAllByCommandeId(int idCommande);
    ProduitCommander save(ProduitCommander produitCommander);
    List<ProduitCommander> saveAll(List<ProduitCommander> produitCommanders);
    void deleteAllByCommande(Commande commande);
    void deleteAllByProduit(Produit produit);
}
