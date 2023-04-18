package com.example.quickdinner.controller;

import com.example.quickdinner.model.Panier;
import com.example.quickdinner.model.Produit;
import com.example.quickdinner.model.ProduitPanier;
import com.example.quickdinner.model.Utilisateur;
import com.example.quickdinner.service.PanierService;
import com.example.quickdinner.service.ProduitPanierService;
import com.example.quickdinner.service.ProduitService;
import com.example.quickdinner.service.UtilisateurService;
import com.example.quickdinner.utils.Jwt;
import com.example.quickdinner.utils.PairPoduitQuantite;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/api")
@CrossOrigin(origins = {"*"})
public class PanierController {

   private final UtilisateurService utilisateurService;
   private final ProduitService produitService;
   private final PanierService panierService;
   private final ProduitPanierService produitPanierService;

    PanierController(UtilisateurService utilisateurService, ProduitService produitService,
                     PanierService panierService, ProduitPanierService produitPanierService) {
        this.utilisateurService = utilisateurService;
        this.produitService = produitService;
        this.panierService = panierService;
        this.produitPanierService = produitPanierService;
    }

    @ApiOperation("Récupère le panier de produit d'un utilisateur")
    @GetMapping("/user/panier")
    public ResponseEntity getPanier(@RequestHeader("Authorization") String token) {
        Optional<Utilisateur> user = Jwt.getUserFromToken(token, utilisateurService);
        if(user == null || !user.isPresent()) {
            return ResponseEntity.badRequest().body("Utilisateur non connecté");
        }

        Utilisateur connectedUser = user.get();

        if(!"Client".equals(connectedUser.getRole().getLibelle())) {
            return ResponseEntity.status(401).body("Vous n'avez pas les droits pour accéder à cette ressource");
        }

        return ResponseEntity.ok(connectedUser.getPanier());
    }

    @ApiOperation("Ajoute un produit au panier d'un utilisateur")
    @PostMapping("/user/panier")
    public ResponseEntity addPanier(@RequestHeader("Authorization") String token,
                                    @RequestBody PairPoduitQuantite pairPoduitQuantite) {

        if(pairPoduitQuantite.getQuantite() <= 0) {
            return ResponseEntity.badRequest().body("le nombre de produit doit être supérieur à 0");
        }

        Optional<Utilisateur> user = Jwt.getUserFromToken(token, utilisateurService);

        if(user == null || !user.isPresent()) {
            return ResponseEntity.badRequest().body("Utilisateur non connecté");
        }

        Utilisateur connectedUser = user.get();

        if(!"Client".equals(connectedUser.getRole().getLibelle())) {
            return ResponseEntity.status(401).body("Vous n'avez pas les droits pour accéder à cette ressource");
        }

        Panier panier = connectedUser.getPanier();
        Produit produit = produitService.findById(pairPoduitQuantite.getId()).orElse(null);

        if(produit == null) {
            return ResponseEntity.badRequest().body("Le produit n'existe pas");
        }

        if(panier.hasProduit(produit)) {
            return ResponseEntity.badRequest().body("Le produit est déjà dans le panier");
        }

        panier.addProduit(ProduitPanier.builder().panier(panier).produit(produit).quantite(pairPoduitQuantite.getQuantite()).build());
        panierService.save(panier);

        return ResponseEntity.status(201).body(null);
    }

    @ApiOperation("Supprime un produit du panier d'un utilisateur")
    @DeleteMapping("/user/panier/{idProduit}")
    public ResponseEntity deletePanier(@RequestHeader("Authorization") String token, @PathVariable Integer idProduit) {

        Optional<Utilisateur> user = Jwt.getUserFromToken(token, utilisateurService);

        if(user == null || !user.isPresent()) {
            return ResponseEntity.badRequest().body("Utilisateur non connecté");
        }

        Utilisateur connectedUser = user.get();

        if(!"Client".equals(connectedUser.getRole().getLibelle())) {
            return ResponseEntity.status(401).body("Vous n'avez pas les droits pour accéder à cette ressource");
        }

        Panier panier = connectedUser.getPanier();
        Produit produit = produitService.findById(idProduit).orElse(null);

        if(produit == null) {
            return ResponseEntity.badRequest().body("Le produit n'existe pas");
        }

        if(!panier.hasProduit(produit)) {
            return ResponseEntity.badRequest().body("Le produit n'est pas dans le panier");
        }

        Optional<ProduitPanier> produitPanier = panier.getProduitsToProduitPanier(produit);

        if(!produitPanier.isPresent()) {
            return ResponseEntity.badRequest().body("Le produit n'est pas dans le panier");
        }

        produitPanierService.deleteProduit(produitPanier.get());

        return ResponseEntity.status(204).body(null);
    }

    @ApiOperation("Modifie la quantité d'un produit du panier d'un utilisateur")
    @PutMapping("/user/panier")
    public ResponseEntity modifyPanier(@RequestHeader("Authorization") String token,
                                       @RequestBody PairPoduitQuantite pairPoduitQuantite) {
        if(pairPoduitQuantite.getQuantite() <= 0) {
            return ResponseEntity.badRequest().body("le nombre de produit doit être supérieur à 0");
        }

        Optional<Utilisateur> user = Jwt.getUserFromToken(token, utilisateurService);

        if(user == null || !user.isPresent()) {
            return ResponseEntity.badRequest().body("Utilisateur non connecté");
        }

        Utilisateur connectedUser = user.get();

        if(!"Client".equals(connectedUser.getRole().getLibelle())) {
            return ResponseEntity.status(401).body("Vous n'avez pas les droits pour accéder à cette ressource");
        }

        Panier panier = connectedUser.getPanier();
        Produit produit = produitService.findById(pairPoduitQuantite.getId()).orElse(null);

        if(produit == null) {
            return ResponseEntity.badRequest().body("Le produit n'existe pas");
        }

        if(!panier.hasProduit(produit)) {
            return ResponseEntity.badRequest().body("Le produit n'est pas dans le panier");
        }

        Optional<ProduitPanier> produitPanier = panier.getProduitsToProduitPanier(produit);

        if(!produitPanier.isPresent()) {
            return ResponseEntity.badRequest().body("Le produit n'est pas dans le panier");
        }

        produitPanier.get().setQuantite(pairPoduitQuantite.getQuantite());
        produitPanierService.save(produitPanier.get());

        return ResponseEntity.status(204).body(null);
    }

    @ApiOperation("Vide le panier d'un utilisateur")
    @DeleteMapping("/user/panier/all")
    public ResponseEntity clearPanier(@RequestHeader("Authorization") String token) {
        Optional<Utilisateur> user = Jwt.getUserFromToken(token, utilisateurService);

        if(user == null || !user.isPresent()) {
            return ResponseEntity.badRequest().body("Utilisateur non connecté");
        }

        Utilisateur connectedUser = user.get();

        if(!"Client".equals(connectedUser.getRole().getLibelle())) {
            return ResponseEntity.status(401).body("Vous n'avez pas les droits pour accéder à cette ressource");
        }

        Panier panier = connectedUser.getPanier();

        if(panier.getProduitPaniers().isEmpty()) {
            return ResponseEntity.badRequest().body("Le panier est déjà vide");
        }

        produitPanierService.deleteAllByPanier(panier);

        return ResponseEntity.status(204).body(null);
    }
}
