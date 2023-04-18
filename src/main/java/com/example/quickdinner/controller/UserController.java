package com.example.quickdinner.controller;

import com.example.quickdinner.model.Commande;
import com.example.quickdinner.model.Panier;
import com.example.quickdinner.model.Utilisateur;
import com.example.quickdinner.service.CommandeService;
import com.example.quickdinner.service.PanierService;
import com.example.quickdinner.service.RoleService;
import com.example.quickdinner.service.UtilisateurService;
import com.example.quickdinner.utils.Jwt;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api")
@CrossOrigin(origins = {"*"})
public class UserController {

    private final UtilisateurService utilisateurService;
    private final RoleService roleService;
    private final PanierService panierService;
    private final CommandeService commandeService;

    public UserController(UtilisateurService utilisateurService,
                          RoleService roleService, PanierService panierService,
                          CommandeService commandeService) {
        this.utilisateurService = utilisateurService;
        this.roleService = roleService;
        this.panierService = panierService;
        this.commandeService = commandeService;
    }

    @ApiOperation("Enregistre un nouvelle utilisateur")
    @ApiImplicitParam(name = "Utilisateur",
            value = "{\"nom\": \"string\", \"prenom\": \"string\", \"email\": \"string\", \"password\": \"string\"}",
            required = true,
            dataType = "object",
            paramType = "body")
    @PostMapping("/user/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> register(@ApiParam(hidden = true) @RequestBody Utilisateur utilisateur) {
        if(utilisateur.getNom() == null || utilisateur.getNom().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Nom is required");
        }
        else if(utilisateur.getPrenom() == null || utilisateur.getPrenom().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Prenom is required");
        }
        else if(utilisateur.getEmail() == null || utilisateur.getEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }
        else if(!utilisateur.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            return ResponseEntity.badRequest().body("Email is not valid");
        }
        else if(utilisateur.getPassword() == null || utilisateur.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Password is required");
        }
        else if(utilisateurService.findByEmail(utilisateur.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        else if(utilisateur.getPassword().length() < 8 || !utilisateur.getPassword().matches(".*\\d.*")
                || !utilisateur.getPassword().matches(".*[a-z].*") || !utilisateur.getPassword().matches(".*[A-Z].*")
                || !utilisateur.getPassword().matches(".*[!@#$%^&*()_+].*")) {
            return ResponseEntity.badRequest().body("Password must be at least 8 characters, contain a number," +
                    " a lowercase, an uppercase letter and a special character");
        }

        Argon2 argon2 = Argon2Factory.create(
                Argon2Factory.Argon2Types.ARGON2id, 32, 64);

        utilisateur.setPassword(argon2.hash(4, 1024 * 1024, 8, utilisateur.getPassword()));
        utilisateur.setRole(roleService.findByLibelle("Client").orElse(null));

        Panier panier = Panier.builder().build();
        panier = panierService.save(panier);

        utilisateur.setPanier(panier);

        utilisateurService.save(utilisateur);
        return ResponseEntity.ok("User registered");
    }

    @ApiOperation("Connecte un utilisateur")
    @ApiImplicitParam(name = "Utilisateur",
            value = "{\"email\": \"string\", \"password\": \"string\"}",
            required = true,
            dataType = "object",
            paramType = "body")
    @PostMapping("/user/login")
    public ResponseEntity<String> login(@ApiParam(hidden = true) @RequestBody Utilisateur utilisateur) {

        if(utilisateur.getEmail() == null || utilisateur.getEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }
        else if(utilisateur.getPassword() == null || utilisateur.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Password is required");
        }

        Argon2 argon2 = Argon2Factory.create(
                Argon2Factory.Argon2Types.ARGON2id, 32, 64);

        Optional<Utilisateur> user = utilisateurService.findByEmail(utilisateur.getEmail());

        if(!user.isPresent()) {
            return ResponseEntity.badRequest().body("Email or password is wrong");
        }

        if(!argon2.verify(user.get().getPassword(), utilisateur.getPassword())) {
            return ResponseEntity.badRequest().body("Email or password is wrong");
        }

        return ResponseEntity.ok(Jwt.generate(user.get()));
    }

    @ApiOperation("vérifie si le token est valide")
    @GetMapping("/user/verify")
    public ResponseEntity<Boolean> checkToken(@RequestHeader("Authorization") String token) {
        Optional<Utilisateur> user = Jwt.getUserFromToken(token, utilisateurService);
        return ResponseEntity.ok(user != null && user.isPresent());
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
    @GetMapping("/user/commandes")
    public ResponseEntity<List<Commande>> getCommandes(@RequestHeader("Authorization") String token) {
        Optional<Utilisateur> user = Jwt.getUserFromToken(token, utilisateurService);
        if(user == null || !user.isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }

        Utilisateur connectedUser = user.get();

        if("Client".equals(connectedUser.getRole().getLibelle())) {
            return ResponseEntity.ok(commandeService.findAllByUtilisateurId(connectedUser.getId()));
        }

        return ResponseEntity.status(401).body(null);
    }
}
