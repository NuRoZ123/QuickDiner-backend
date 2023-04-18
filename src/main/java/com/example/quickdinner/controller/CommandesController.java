package com.example.quickdinner.controller;

import com.example.quickdinner.model.Commande;
import com.example.quickdinner.model.Produit;
import com.example.quickdinner.model.ProduitCommander;
import com.example.quickdinner.model.Utilisateur;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
            return ResponseEntity.ok(commandeService.findAllByUtilisateurId(connectedUser.getId()));
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
                                .build())
                .collect(Collectors.toList());


        produitCommanderService.saveAll(produitCommanders);

        return ResponseEntity.ok().build();
    }

}
