package com.example.quickdinner.service.impl;

import com.example.quickdinner.model.Commande;
import com.example.quickdinner.model.Commercant;
import com.example.quickdinner.model.Produit;
import com.example.quickdinner.model.Utilisateur;
import com.example.quickdinner.repository.UtilisateurRepository;
import com.example.quickdinner.service.*;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    private final PanierService panierService;
    private final ProduitPanierService produitPanierService;
    private final ProduitCommanderService produitCommanderService;
    private final CommandeService commandeService;
    private final CommentaireCommercantsService commentaireCommercantsService;
    private final CommercantService commercantService;
    private final ProduitService produitService;

    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository, PanierService panierService,
                                  ProduitPanierService produitPanierService, ProduitCommanderService produitCommanderService,
                                  CommandeService commandeService, CommentaireCommercantsService commentaireCommercantsService,
                                  CommercantService commercantService, ProduitService produitService) {
        this.utilisateurRepository = utilisateurRepository;
        this.panierService = panierService;
        this.produitPanierService = produitPanierService;
        this.produitCommanderService = produitCommanderService;
        this.commandeService = commandeService;
        this.commentaireCommercantsService = commentaireCommercantsService;
        this.commercantService = commercantService;
        this.produitService = produitService;
    }

    @Override
    public Optional<Utilisateur> findByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    @Override
    public Utilisateur save(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public void deleteAdmin(Utilisateur utilisateur) {
        utilisateurRepository.delete(utilisateur);
    }

    @Override
    public void deleteClient(Utilisateur utilisateur) {
        produitPanierService.deleteAllByPanier(utilisateur.getPanier());

        List<Commande> commandes = commandeService.findAllByUtilisateurId(utilisateur.getId());
        commandes.forEach(commande -> {
            produitCommanderService.deleteAllByCommande(commande);
            commandeService.delete(commande);
        });

        commentaireCommercantsService.deleteAllByUtilisateur(utilisateur);
        panierService.delete(utilisateur.getPanier());
        utilisateurRepository.delete(utilisateur);
    }

    @Override
    public void deleteCommercant(Utilisateur utilisateur) {
        Optional<Commercant> commercant = commercantService.findByUtilisateurId(utilisateur.getId());
        if(commercant.isPresent()) {
            List<Produit> produits = produitService.findAllByCommercant(commercant.get().getId());

            produits.forEach(produit -> {
                produitPanierService.deleteAllByProduit(produit);
                produitCommanderService.deleteAllByProduit(produit);
                produitService.delete(produit);
            });

            commentaireCommercantsService.deleteAllByCommercant(commercant.get());
            commercantService.deleteByManager(utilisateur);
        }

        utilisateurRepository.delete(utilisateur);
    }
}
