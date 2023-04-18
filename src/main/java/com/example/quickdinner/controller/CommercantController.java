package com.example.quickdinner.controller;

import com.example.quickdinner.model.*;
import com.example.quickdinner.service.*;
import com.example.quickdinner.utils.Jwt;
import com.example.quickdinner.utils.PairNoteRestaurant;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

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

            commercants.add(new PairNoteRestaurant(note, commercant));
        });

        return ResponseEntity.ok(commercants);
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

        System.out.println(commandesAvecDoublon);

        List<Commande> commandes = new ArrayList<>();

        for(Commande commande : commandesAvecDoublon) {
            if(!commandes.contains(commande)) {
                commandes.add(commande);
            }
        }

        System.out.println(commandes);

        commandes.forEach(commande -> {
            List<ProduitCommander> lesProduitsDuResteauSeulement = new ArrayList<>();

            commande.getProduitsCommander().forEach(produitCommander -> {
                if(produits.contains(produitCommander.getProduit())) {
                    lesProduitsDuResteauSeulement.add(produitCommander);
                }
            });

            commande.setProduitsCommander(lesProduitsDuResteauSeulement);
        });


        return ResponseEntity.ok(commandes);
    }

}
