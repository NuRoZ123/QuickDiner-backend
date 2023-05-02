package com.example.quickdinner.controller;

import com.example.quickdinner.QuickDinnerApplication;
import com.example.quickdinner.model.Commande;
import com.example.quickdinner.model.Produit;
import com.example.quickdinner.model.ProduitCommander;
import com.example.quickdinner.model.Utilisateur;
import com.example.quickdinner.model.enumeration.EtatProduitCommande;
import com.example.quickdinner.service.CommandeService;
import com.example.quickdinner.service.ProduitCommanderService;
import com.example.quickdinner.service.ProduitService;
import com.example.quickdinner.service.UtilisateurService;
import com.example.quickdinner.utils.Jwt;
import com.example.quickdinner.utils.PairPoduitQuantite;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api")
@CrossOrigin(origins = {"*"})
public class CommandesController {

    private final UtilisateurService utilisateurService;
    private final CommandeService commandeService;
    private final ProduitService produitService;
    private final ProduitCommanderService produitCommanderService;

    public CommandesController(UtilisateurService utilisateurService, CommandeService commandeService,
                               ProduitService produitService, ProduitCommanderService produitCommanderService) {
        this.utilisateurService = utilisateurService;
        this.commandeService = commandeService;
        this.produitService = produitService;
        this.produitCommanderService = produitCommanderService;
    }

    @ApiOperation("Récupère les commandes d'un utilisateur: Si l'utilisateur est un client")
    @GetMapping("/user/commandes")
    public ResponseEntity getCommandes(@RequestHeader("Authorization") String token) {
        Optional<Utilisateur> user = Jwt.getUserFromToken(token, utilisateurService);
        if(user == null || !user.isPresent()) {
            return ResponseEntity.badRequest().body("Utilisateur non connecté");
        }

        Utilisateur connectedUser = user.get();

        if("Client".equals(connectedUser.getRole().getLibelle())) {
            List<Commande> commandes = commandeService.findAllByUtilisateurId(connectedUser.getId());
            commandes.forEach(commande -> {
                commande.getProduitsCommander().forEach(produitCommander -> {
                    produitCommander.getProduit().setImage(QuickDinnerApplication.getHost() + "/api/produits/" + produitCommander.getProduit().getId() + "/image");
                });
            });

            return ResponseEntity.ok(commandes);
        }

        return ResponseEntity.status(401).body(null);
    }

    @ApiOperation("Permet à l'utilisateur de passer une commande")
    @PostMapping("/user/commandes")
    public ResponseEntity createCommande(@RequestHeader("Authorization") String token,
                                         @RequestBody List<PairPoduitQuantite> produits) {
        Optional<Utilisateur> user = Jwt.getUserFromToken(token, utilisateurService);
        if(user == null || !user.isPresent()) {
            return ResponseEntity.badRequest().body("Utilisateur non connecté");
        }

        Utilisateur connectedUser = user.get();

        if(!"Client".equals(connectedUser.getRole().getLibelle())) {
            return ResponseEntity.status(401).body("Vous n'avez pas les droits pour accéder à cette ressource");
        }

        Commande commande = Commande.builder()
                .utilisateur(connectedUser)
                .build();

        commande = commandeService.save(commande);
        Map<Integer, Produit> produitsMap = new HashMap<>();

        Commande finalCommande = commande;
        List<ProduitCommander> produitCommanders = produits.stream()
                .filter(pairPoduitQuantite -> {
                    Produit produit = produitService.findById(pairPoduitQuantite.getId()).orElse(null);
                    if(produit != null) {
                        produitsMap.put(pairPoduitQuantite.getId(), produit);
                        return true;
                    }
                    return false;
                })
                .map(pairPoduitQuantite ->
                        ProduitCommander.builder()
                                .commande(finalCommande)
                                .produit(produitsMap.get(pairPoduitQuantite.getId()))
                                .quantite(pairPoduitQuantite.getQuantite())
                                .etat(EtatProduitCommande.WIP.getLibelle())
                                .build())
                .collect(Collectors.toList());

        produitCommanderService.saveAll(produitCommanders);

        // websocket notification
        QuickDinnerApplication.commandesQueue.add(finalCommande.getId());

        if(QuickDinnerApplication.commandeQueuObserver != null) {
            QuickDinnerApplication.commandeQueuObserver.update();
        }

        return ResponseEntity.ok().build();
    }


    @ApiOperation("Permet de définir la commande en terminé")
    @PutMapping("/user/commandes/{id}/termine")
    public ResponseEntity setCommandeTermine(@RequestHeader("Authorization") String token,
                                         @PathVariable("id") Integer id) {
        Optional<Utilisateur> user = Jwt.getUserFromToken(token, utilisateurService);
        if(user == null || !user.isPresent()) {
            return ResponseEntity.badRequest().body("Utilisateur non connecté");
        }

        Utilisateur connectedUser = user.get();

        if(!"Commercant".equals(connectedUser.getRole().getLibelle())) {
            return ResponseEntity.status(401).body("Vous n'avez pas les droits pour accéder à cette ressource");
        }

        Optional<Commande> commandeOpt = commandeService.findById(id);

        if(commandeOpt == null || !commandeOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Commande non trouvée");
        }

        Commande commande = commandeOpt.get();
        commande.getProduitsCommander().forEach(produitCommander -> {
            if(Objects.equals(produitCommander.getProduit().getCommercant().getManager().getId(), connectedUser.getId())) {
                produitCommander.setEtat(EtatProduitCommande.DONE.getLibelle());

                produitCommanderService.save(produitCommander);
            }
        });



        // Check si la commande est completer par tlm
        boolean isDone = commande.getProduitsCommander().stream()
                .allMatch(produitCommander -> EtatProduitCommande.DONE.getLibelle().equals(produitCommander.getEtat()));

        if(isDone) {
            QuickDinnerApplication.commandesTerminer.add(commande.getId());
            QuickDinnerApplication.playerCommandeObserver.update();
        }

        return ResponseEntity.ok().build();
    }

}
