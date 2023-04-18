package com.example.quickdinner.controller;

import com.example.quickdinner.model.Commercant;
import com.example.quickdinner.model.Produit;
import com.example.quickdinner.model.Utilisateur;
import com.example.quickdinner.service.*;
import com.example.quickdinner.utils.Jwt;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api")
@CrossOrigin(origins = {"*"})
public class ProduitController {

    private ProduitService produitService;
    private CommercantService commercantService;
    private UtilisateurService utilisateurService;
    private ProduitCommanderService produitCommanderService;
    private ProduitPanierService produitPanierService;

    ProduitController(ProduitService produitService, CommercantService commercantService,
                      UtilisateurService utilisateurService, ProduitCommanderService produitCommanderService,
                      ProduitPanierService produitPanierService) {
        this.produitService = produitService;
        this.commercantService = commercantService;
        this.utilisateurService = utilisateurService;
        this.produitCommanderService = produitCommanderService;
        this.produitPanierService = produitPanierService;
    }

    @ApiOperation(value = "Get all produits (menu) by restaurant",
            response = List.class)
    @GetMapping("/produits/{idRestaurant}")
    public ResponseEntity<List<Produit>> findAllProduitsByRestaurant(@PathVariable Integer idRestaurant) {
        return ResponseEntity.ok(produitService.findAllByCommercant(idRestaurant));
    }

    @ApiOperation(value = "Manager can create a new produit")
    @ApiImplicitParam(name = "Produits",
            value = "[{\"nom\": \"string\", \"prix\": float, \"description\": \"string\"}]",
            required = true,
            dataType = "object",
            paramType = "body")
    @PostMapping("/produits")
    public ResponseEntity createProduit(@RequestHeader("Authorization") String token, @ApiParam(hidden = true) @RequestBody List<Produit> produits) {

        Optional<Utilisateur> user = Jwt.getUserFromToken(token, utilisateurService);
        if(user == null || !user.isPresent()) {
            return ResponseEntity.badRequest().body("Utilisateur non connecté");
        }

        Utilisateur connectedUser = user.get();

        if(!"Commercant".equals(connectedUser.getRole().getLibelle())) {
            return ResponseEntity.badRequest().body("Vous n'avez pas les droits pour accéder à cette ressource");
        }

        Optional<Commercant> optionalCommercant = commercantService.findByUtilisateurId(connectedUser.getId());

        if(optionalCommercant == null || !optionalCommercant.isPresent()) {
            return ResponseEntity.badRequest().body("Utilisateur non commercant");
        }

        produits = produits.stream()
                .filter(produit -> produit != null && produit.getId() == null && (produit.getNom() != null && !produit.getNom().isEmpty())
                        && produit.getPrix() != null && (produit.getCommercant() == null))
                .collect(Collectors.toList());

        if(produits.isEmpty()) {
            return ResponseEntity.badRequest().body("Produits invalides");
        }

        produits.forEach(produit -> {
            produit.setCommercant(optionalCommercant.get());
            produitService.save(produit);
        });

        return ResponseEntity.status(201).body(null);
    }

    @ApiOperation(value = "Manager delete a produit")
    @DeleteMapping("/produits/{idProduit}")
    public ResponseEntity deleteProduit(@RequestHeader("Authorization") String token, @PathVariable Integer idProduit) {

        Optional<Utilisateur> user = Jwt.getUserFromToken(token, utilisateurService);
        if(user == null || !user.isPresent()) {
            return ResponseEntity.badRequest().body("Utilisateur non connecté");
        }

        Utilisateur connectedUser = user.get();

        if(!"Commercant".equals(connectedUser.getRole().getLibelle())) {
            return ResponseEntity.badRequest().body("Vous n'avez pas les droits pour accéder à cette ressource");
        }

        Optional<Produit> produitOpt = produitService.findById(idProduit);

        if(produitOpt == null || !produitOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Produit not found");
        }

        Produit produit = produitOpt.get();
        Optional<Commercant> commercantOpt = commercantService.findByUtilisateurId(connectedUser.getId());

        if(commercantOpt == null || !commercantOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Restaurant not found");
        }

        Commercant commercant = commercantOpt.get();
        boolean produitIsFromCommercant = produitService.findAllByCommercant(commercant.getId()).stream()
                .anyMatch(p -> Objects.equals(p.getId(), produit.getId()));

        if(!produitIsFromCommercant) {
            return ResponseEntity.badRequest().body("Vous n'avez pas les droits pour accéder à cette ressource");
        }

        produitPanierService.deleteAllByProduit(produit);
        produitCommanderService.deleteAllByProduit(produit);
        produitService.delete(produit);

        return ResponseEntity.status(200).body(null);
    }

    @ApiOperation(value = "Manager update a produit")
    @PutMapping("/produits")
    public ResponseEntity updateProduit(@RequestHeader("Authorization") String token, @RequestBody Produit produit) {
        Optional<Utilisateur> user = Jwt.getUserFromToken(token, utilisateurService);
        if(user == null || !user.isPresent()) {
            return ResponseEntity.badRequest().body("Utilisateur non connecté");
        }

        Utilisateur connectedUser = user.get();

        if(!"Commercant".equals(connectedUser.getRole().getLibelle())) {
            return ResponseEntity.badRequest().body("Vous n'avez pas les droits pour accéder à cette ressource");
        }

        Optional<Produit> produitOpt = produitService.findById(produit.getId());

        if(produitOpt == null || !produitOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Produit not found");
        }

        Produit modifyProduit = produitOpt.get();
        Optional<Commercant> commercantOpt = commercantService.findByUtilisateurId(connectedUser.getId());

        if(commercantOpt == null || !commercantOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Restaurant not found");
        }

        Commercant commercant = commercantOpt.get();
        boolean produitIsFromCommercant = produitService.findAllByCommercant(commercant.getId()).stream()
                .anyMatch(p -> Objects.equals(p.getId(), modifyProduit.getId()));

        if(!produitIsFromCommercant) {
            return ResponseEntity.badRequest().body("Vous n'avez pas les droits pour accéder à cette ressource");
        }

        if(produit.getNom() != null && !produit.getNom().isEmpty()) {
            modifyProduit.setNom(produit.getNom());
        }

        if(produit.getPrix() != null) {
            modifyProduit.setPrix(produit.getPrix());
        }

        if(produit.getDescription() != null && !produit.getDescription().isEmpty()) {
            modifyProduit.setDescription(produit.getDescription());
        }

        produitService.save(modifyProduit);

        return ResponseEntity.status(200).body(null);
    }
}
