package com.example.quickdinner.controller;

import com.example.quickdinner.QuickDinnerApplication;
import com.example.quickdinner.model.Commercant;
import com.example.quickdinner.model.Produit;
import com.example.quickdinner.model.Utilisateur;
import com.example.quickdinner.model.enumeration.TypeCompteUtilisateur;
import com.example.quickdinner.service.*;
import com.example.quickdinner.utils.Jwt;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
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
        List<Produit> produits = produitService.findAllByCommercant(idRestaurant);

        produits.forEach(produit -> {
            produit.setImage(QuickDinnerApplication.getHost() + "/api/produits/" + produit.getId() + "/image");
        });

        return ResponseEntity.ok(produits);
    }

    @ApiOperation(value = "Manager can create a new produit")
    @ApiImplicitParam(name = "Produits",
            value = "[{\"nom\": \"string\", \"prix\": float, \"description\": \"string\", \"image\": \"string\"}]",
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

        if(!TypeCompteUtilisateur.Commercant.getType().equals(connectedUser.getRole().getLibelle())) {
            return ResponseEntity.badRequest().body("Vous n'avez pas les droits pour accéder à cette ressource");
        }

        Optional<Commercant> optionalCommercant = commercantService.findByUtilisateurId(connectedUser.getId());

        if(optionalCommercant == null || !optionalCommercant.isPresent()) {
            return ResponseEntity.badRequest().body("Utilisateur non commercant");
        }

        Commercant commercant = optionalCommercant.get();

        produits = produits.stream()
                .filter(produit -> produit != null && produit.getId() == null && (produit.getNom() != null && !produit.getNom().isEmpty())
                        && produit.getPrix() != null && (produit.getCommercant() == null)
                        && (produit.getImage() != null && !produit.getImage().isEmpty()))
                .collect(Collectors.toList());

        if(produits.isEmpty()) {
            return ResponseEntity.badRequest().body("Produits invalides");
        }

        produits.forEach(produit -> {
            produit.setCommercant(commercant);
            produit.setPrix(((float) Math.round(produit.getPrix() * 100)) / 100);
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

        if(!TypeCompteUtilisateur.Commercant.getType().equals(connectedUser.getRole().getLibelle())) {
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

        boolean produitIsFromCommercant = commercant.getProduits().stream()
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

        if(!TypeCompteUtilisateur.Commercant.getType().equals(connectedUser.getRole().getLibelle())) {
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

        if(produit.getImage() != null && !produit.getImage().isEmpty()) {
            modifyProduit.setImage(produit.getImage());
        }

        produitService.save(modifyProduit);

        return ResponseEntity.status(200).body(null);
    }

    @ApiOperation(value = "Récupèrer l'image du produits par son id")
    @GetMapping(value = "/produits/{id}/image", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity getImage(@PathVariable("id") int id) {
        Optional<Produit> produit = produitService.findById(id);
        if(!produit.isPresent()) {
            return ResponseEntity.badRequest().body("Produit non trouvé");
        }

        String b64 = produit.get().getImage();

        if(b64 == null) {
            return ResponseEntity.badRequest().body("Produit sans image");
        }

        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(b64.substring(b64.indexOf(",") + 1));

        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes);
    }

}
