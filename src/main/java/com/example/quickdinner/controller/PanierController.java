package com.example.quickdinner.controller;

import com.example.quickdinner.model.*;
import com.example.quickdinner.service.PanierService;
import com.example.quickdinner.service.ProduitService;
import com.example.quickdinner.service.UtilisateurService;
import com.example.quickdinner.utils.Jwt;
import com.example.quickdinner.utils.PairPoduitQuantite;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api")
@CrossOrigin(origins = {"*"})
public class PanierController {

   private final UtilisateurService utilisateurService;
   private final ProduitService produitService;
   private final PanierService panierService;

    PanierController(UtilisateurService utilisateurService, ProduitService produitService,
                     PanierService panierService) {
        this.utilisateurService = utilisateurService;
        this.produitService = produitService;
        this.panierService = panierService;
    }

    @ApiOperation("Récupère le panier de produit d'un utilisateur")
    @GetMapping("/user/panier")
    public ResponseEntity<Panier> getPanier(@RequestHeader("Authorization") String token) {
        Optional<Utilisateur> user = Jwt.getUserFromToken(token, utilisateurService);
        if(user == null || !user.isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }

        Utilisateur connectedUser = user.get();

        if(!"Client".equals(connectedUser.getRole().getLibelle())) {
            return ResponseEntity.status(401).body(null);
        }

        return ResponseEntity.ok(connectedUser.getPanier());
    }

    @ApiOperation("Récupère les commandes d'un utilisateur: Si l'utilisateur est un client")
    @PostMapping("/user/panier")
    public ResponseEntity<List<Commande>> addPanier(@RequestHeader("Authorization") String token, @RequestBody PairPoduitQuantite pairPoduitQuantite) {

        Optional<Utilisateur> user = Jwt.getUserFromToken(token, utilisateurService);

        if(user == null || !user.isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }

        Utilisateur connectedUser = user.get();

        if(!"Client".equals(connectedUser.getRole().getLibelle())) {
            return ResponseEntity.status(401).body(null);
        }

        Panier panier = connectedUser.getPanier();
        Produit produit = produitService.findById(pairPoduitQuantite.getId()).orElse(null);

        if(produit == null) {
            return ResponseEntity.badRequest().body(null);
        }

        if(panier.hasProduit(produit)) {
            return ResponseEntity.badRequest().body(null);
        }

        panier.addProduit(ProduitPanier.builder().panier(panier).produit(produit).quantite(pairPoduitQuantite.getQuantite()).build());
        panierService.save(panier);

        return ResponseEntity.status(201).body(null);
    }
}
