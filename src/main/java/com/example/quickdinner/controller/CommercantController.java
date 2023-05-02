package com.example.quickdinner.controller;

import com.example.quickdinner.QuickDinnerApplication;
import com.example.quickdinner.model.*;
import com.example.quickdinner.service.*;
import com.example.quickdinner.utils.Jwt;
import com.example.quickdinner.utils.PairNoteRestaurant;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api")
@CrossOrigin(origins = {"*"})
public class CommercantController {

    private final CommercantService commercantService;
    private final CommentaireCommercantsService commentaireCommercantsService;
    private final UtilisateurService utilisateurService;
    private final ProduitService produitService;
    private final ProduitCommanderService produitCommanderService;

    public CommercantController(CommercantService commercantService,
            CommentaireCommercantsService commentaireCommercantsService,
            UtilisateurService utilisateurService, ProduitService produitService,
            ProduitCommanderService produitCommanderService) {
        this.commercantService = commercantService;
        this.commentaireCommercantsService = commentaireCommercantsService;
        this.utilisateurService = utilisateurService;
        this.produitService = produitService;
        this.produitCommanderService = produitCommanderService;
    }

    @ApiOperation(value = "recupère tous les commerçans (restaurant)",
    response = List.class)
    @GetMapping("/restaurants")
    public ResponseEntity<List<PairNoteRestaurant>> getRestaurant() {
        List<PairNoteRestaurant> commercants = new ArrayList<>();

        commercantService.findAll().forEach(commercant -> {

            Float note = commentaireCommercantsService.findNote(commercant.getId());

            commercant.setImage(QuickDinnerApplication.getHost() + "/api/restaurants/" + commercant.getId() + "/image");

            commercants.add(new PairNoteRestaurant(note, commercant));
        });

        return ResponseEntity.ok(commercants);
    }

    @ApiOperation(value = "recupère les infos du restaurant du commercant connecté")
    @GetMapping("/restaurants/own")
    public ResponseEntity getInfoResteau(@RequestHeader("Authorization") String token) {
        Optional<Utilisateur> user = Jwt.getUserFromToken(token, utilisateurService);

        if(user == null || !user.isPresent()) {
            return ResponseEntity.badRequest().body("Utilisateur non connecté");
        }

        Utilisateur connectedUser = user.get();

        if(!"Commercant".equals(connectedUser.getRole().getLibelle())) {
            return ResponseEntity.status(401).body("Vous n'avez pas les droits pour accéder à cette ressource");
        }

        Optional<Commercant> commercantOpt = commercantService.findByUtilisateurId(connectedUser.getId());

        if(!commercantOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Restaurant non trouvé");
        }

        Commercant commercant = commercantOpt.get();

        commercant.setImage(QuickDinnerApplication.getHost() + "/api/restaurants/" + commercant.getId() + "/image");

        return ResponseEntity.ok(commercant);
    }

    @ApiOperation(value = "recupère toutes les commandes du restaurant",
    response = List.class)
    @GetMapping("/restaurants/commandes")
    public ResponseEntity getCommandes(@RequestHeader("Authorization") String token) {
        Optional<Utilisateur> user = Jwt.getUserFromToken(token, utilisateurService);

        if(user == null || !user.isPresent()) {
            return ResponseEntity.badRequest().body("Utilisateur non connecté");
        }

        Utilisateur connectedUser = user.get();

        if(!"Commercant".equals(connectedUser.getRole().getLibelle())) {
            return ResponseEntity.status(401).body("Vous n'avez pas les droits pour accéder à cette ressource");
        }

        Optional<Commercant> commercantOpt = commercantService.findByUtilisateurId(connectedUser.getId());

        if(!commercantOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Restaurant non trouvé");
        }

        Commercant commercant = commercantOpt.get();
        List<Produit> produits = produitService.findAllByCommercant(commercant.getId());
        List<ProduitCommander> produitsCommander = new ArrayList<>();
        produits.forEach(produit -> {
            produitsCommander.addAll(produitCommanderService.findAllByProduitId(produit.getId()));
        });

        List<Commande> commandesAvecDoublon = new ArrayList<>();
        produitsCommander.forEach(produitCommander -> {
            commandesAvecDoublon.add(produitCommander.getCommande());
        });

        List<Commande> commandes = new ArrayList<>();

        for(Commande commande : commandesAvecDoublon) {
            if(!commandes.contains(commande)) {
                commandes.add(commande);
            }
        }

        commandes.forEach(commande -> {
            List<ProduitCommander> lesProduitsDuResteauSeulement = new ArrayList<>();

            commande.getProduitsCommander().forEach(produitCommander -> {
                if(produits.contains(produitCommander.getProduit())) {
                    produitCommander.getProduit().setImage(QuickDinnerApplication.getHost() + "/api/produits/" + produitCommander.getProduit().getId() + "/image");
                    lesProduitsDuResteauSeulement.add(produitCommander);
                }
            });

            commande.setProduitsCommander(lesProduitsDuResteauSeulement);
        });


        return ResponseEntity.ok(commandes);
    }

    @ApiOperation(value = "recupère les infos du restaurant")
    @GetMapping("/restaurants/{id}")
    public ResponseEntity getRestaurantInfo(@PathVariable("id") int id) {
        Optional<Commercant> commercantOpt = commercantService.findById(id);
        if(!commercantOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Restaurant non trouvé");
        }

        Commercant commercant = commercantOpt.get();

        Float note = commentaireCommercantsService.findNote(commercant.getId());
        commercant.setImage(QuickDinnerApplication.getHost() + "/api/restaurants/" + commercant.getId() + "/image");

        return ResponseEntity.ok(new PairNoteRestaurant(note, commercant));
    }

    @ApiOperation(value = "Récupèrer l'image du restau par son id")
    @GetMapping(value = "/restaurants/{id}/image", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity getImage(@PathVariable("id") int id) {
        Optional<Commercant> commercant = commercantService.findById(id);
        if(!commercant.isPresent()) {
            return ResponseEntity.badRequest().body("Restaurant non trouvé");
        }

        String b64 = commercant.get().getImage();

        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(b64.substring(b64.indexOf(",") + 1));

        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes);
    }

    @ApiOperation(value = "Permet de modifier les infos du restaurant")
    @PutMapping("/restaurants")
    @ApiImplicitParam(name = "Produits",
            value = "{\"adresse\": \"string\", \"image\": \"string\", \"nom\": \"string\"}",
            required = true,
            dataType = "object",
            paramType = "body")
    public ResponseEntity updateRestaurant(@RequestHeader("Authorization") String token,
                                           @ApiParam(hidden = true) @RequestBody Commercant commercant) {
        Optional<Utilisateur> user = Jwt.getUserFromToken(token, utilisateurService);

        if(user == null || !user.isPresent()) {
            return ResponseEntity.badRequest().body("Utilisateur non connecté");
        }

        Utilisateur connectedUser = user.get();

        if(!"Commercant".equals(connectedUser.getRole().getLibelle())) {
            return ResponseEntity.status(401).body("Vous n'avez pas les droits pour accéder à cette ressource");
        }

        Optional<Commercant> commercantOpt = commercantService.findByUtilisateurId(connectedUser.getId());

        if(!commercantOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Restaurant non trouvé");
        }

        Commercant commercantToUpdate = commercantOpt.get();

        if(commercant.getAdresse() != null && !"".equals(commercant.getAdresse().trim())) {
            commercantToUpdate.setAdresse(commercant.getAdresse());
        }

        if(commercant.getImage() != null && !"".equals(commercant.getImage().trim())) {
            commercantToUpdate.setImage(commercant.getImage());
        }

        if(commercant.getNom() != null && !"".equals(commercant.getNom().trim())) {
            commercantToUpdate.setNom(commercant.getNom());
        }

        commercantService.save(commercantToUpdate);

        return ResponseEntity.ok().build();
    }

}
