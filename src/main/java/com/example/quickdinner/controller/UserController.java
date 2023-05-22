package com.example.quickdinner.controller;

import com.example.quickdinner.model.*;
import com.example.quickdinner.model.enumeration.TypeCompteUtilisateur;
import com.example.quickdinner.service.*;
import com.example.quickdinner.utils.Jwt;
import com.example.quickdinner.utils.TripleUtilisateurCommercantType;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api")
@CrossOrigin(origins = {"*"})
public class UserController {

    private final Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2i, 16, 32);

    private final UtilisateurService utilisateurService;
    private final RoleService roleService;
    private final PanierService panierService;
    private final CommercantService commercantService;
    private final CommandeService commandeService;
    private final CommentaireCommercantsService commentaireCommercantsService;

    public UserController(UtilisateurService utilisateurService, RoleService roleService,
                          PanierService panierService, CommercantService commercantService,
                          CommentaireCommercantsService commentaireCommercantsService,
                          CommandeService commandeService) {
        this.utilisateurService = utilisateurService;
        this.roleService = roleService;
        this.panierService = panierService;
        this.commercantService = commercantService;
        this.commentaireCommercantsService = commentaireCommercantsService;
        this.commandeService = commandeService;
    }

    @ApiOperation("Enregistre un nouvelle utilisateur")
    @ApiImplicitParam(name = "Utilisateur et Restaurant",
            value = "{\"utilisateur\": " +
                    "{\"nom\": \"string\", \"prenom\": \"string\", \"email\": \"string\", \"password\": \"string\"}," +
                    "\"restaurant\": " +
                    "{\"nom\": \"string\", \"adresse\": \"string\", \"image\": \"string\"}, " +
                    "\"type\": \"string\"}",
            required = true,
            dataType = "object",
            paramType = "body")
    @PostMapping("/user/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> register(@ApiParam(hidden = true) @RequestBody TripleUtilisateurCommercantType pair) {
        Utilisateur utilisateur = pair.getUtilisateur();
        Commercant restaurant = pair.getRestaurant();
        String type = pair.getType();

        if(utilisateur.getId() != null) {
            return ResponseEntity.badRequest().body("Id is not required");
        }

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

        utilisateur.setPassword(argon2.hash(4, 2048*2, 8, utilisateur.getPassword().toCharArray()));

        if(TypeCompteUtilisateur.Commercant.getType().equals(type)) {
            utilisateur.setRole(roleService.findByLibelle(TypeCompteUtilisateur.Commercant.getType()).orElse(null));

            if(restaurant == null) {
                return ResponseEntity.badRequest().body("Restaurant is required");
            }

            if(restaurant.getId() != null) {
                return ResponseEntity.badRequest().body("Id is not required");
            }

            if(restaurant.getNom() == null || restaurant.getNom().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Nom of restaurant is required");
            }

            if(restaurant.getAdresse() == null || restaurant.getAdresse().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Adresse of restaurant is required");
            }

            if(restaurant.getImage() == null || restaurant.getImage().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Image of restaurant is required");
            }

            Utilisateur newUser = utilisateurService.save(utilisateur);

            restaurant.setManager(newUser);
            commercantService.save(restaurant);

        } else if(TypeCompteUtilisateur.Client.getType().equals(type)) {
            utilisateur.setRole(roleService.findByLibelle(TypeCompteUtilisateur.Client.getType()).orElse(null));

            Panier panier = Panier.builder().build();
            panier = panierService.save(panier);

            utilisateur.setPanier(panier);
            utilisateurService.save(utilisateur);
        } else {
            return ResponseEntity.badRequest().body("Type is not valid");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(Jwt.generate(utilisateur));
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

        Optional<Utilisateur> user = utilisateurService.findByEmail(utilisateur.getEmail());

        if(!user.isPresent()) {
            return ResponseEntity.badRequest().body("Identifiants ou mot de passe incorrect");
        }

        if(!argon2.verify(user.get().getPassword(), utilisateur.getPassword().toCharArray())) {
            return ResponseEntity.badRequest().body("Identifiants ou mot de passe incorrect");
        }

        return ResponseEntity.ok(Jwt.generate(user.get()));
    }

    @ApiOperation("vérifie si le token est valide")
    @GetMapping("/user/verify")
    public ResponseEntity<Boolean> checkToken(@RequestHeader("Authorization") String token) {
        Optional<Utilisateur> user = Jwt.getUserFromToken(token, utilisateurService);
        return ResponseEntity.ok(user != null && user.isPresent());
    }

    @ApiOperation("Supprime l'utilisateur connecté")
    @DeleteMapping("/user")
    @Transactional
    public ResponseEntity delete(@RequestHeader("Authorization") String token) {
        Optional<Utilisateur> user = Jwt.getUserFromToken(token, utilisateurService);
        if(user == null || !user.isPresent()) {
            return ResponseEntity.badRequest().body("Utilisateur non connecté");
        }

        Utilisateur connectedUser = user.get();

        if(TypeCompteUtilisateur.Admin.getType().equals(connectedUser.getRole().getLibelle())) {
            utilisateurService.deleteAdmin(connectedUser);
        } else if(TypeCompteUtilisateur.Client.getType().equals(connectedUser.getRole().getLibelle())) {
            utilisateurService.deleteClient(connectedUser);
        } else {
            utilisateurService.deleteCommercant(connectedUser);
        }

        return ResponseEntity.ok("Utilisateur supprimé");
    }

    @ApiOperation("Modifie l'utilisateur connecté")
    @ApiImplicitParam(name = "Utilisateur",
            value = "{\"nom\": \"string\", \"prenom\": \"string\", \"email\": \"string\", \"password\": \"string\"}",
            required = true,
            dataType = "object",
            paramType = "body")
    @PutMapping("/user")
    public ResponseEntity modify(@RequestHeader("Authorization") String token,
                                 @ApiParam(hidden = true) @RequestBody Utilisateur utilisateur) {
        Optional<Utilisateur> user = Jwt.getUserFromToken(token, utilisateurService);
        if(user == null || !user.isPresent()) {
            return ResponseEntity.badRequest().body("Utilisateur non connecté");
        }

        Utilisateur connectedUser = user.get();
        Optional<Utilisateur> emailExistUser = utilisateurService.findByEmail(utilisateur.getEmail());

        if(utilisateur.getEmail() != null && !utilisateur.getEmail().trim().isEmpty()) {

            if(utilisateur.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
                    && (!emailExistUser.isPresent() || emailExistUser.get().getId().equals(connectedUser.getId()))) {
                connectedUser.setEmail(utilisateur.getEmail());
            } else {
                return ResponseEntity.badRequest().body("Email already exists");
            }
        }

        if(utilisateur.getNom() != null && !utilisateur.getNom().trim().isEmpty()) {
            connectedUser.setNom(utilisateur.getNom());
        }

        if(utilisateur.getPrenom() != null && !utilisateur.getPrenom().trim().isEmpty()) {
            connectedUser.setPrenom(utilisateur.getPrenom());
        }

        if(utilisateur.getPassword() != null && !utilisateur.getPassword().trim().isEmpty()) {
            if(utilisateur.getPassword().length() < 8 || !utilisateur.getPassword().matches(".*\\d.*")
                    || !utilisateur.getPassword().matches(".*[a-z].*") || !utilisateur.getPassword().matches(".*[A-Z].*")
                    || !utilisateur.getPassword().matches(".*[!@#$%^&*()_+].*")) {
                return ResponseEntity.badRequest().body("Password must be at least 8 characters, contain a number," +
                        " a lowercase, an uppercase letter and a special character");
            }

            connectedUser.setPassword(argon2.hash(4, 2048*2, 8, utilisateur.getPassword().toCharArray()));
        }

        utilisateurService.save(connectedUser);
        return ResponseEntity.ok(Jwt.generate(connectedUser));
    }

    @ApiOperation("Récupère l'utilisateur connecté")
    @GetMapping("/user")
    public ResponseEntity<Utilisateur> get(@RequestHeader("Authorization") String token) {
        Optional<Utilisateur> user = Jwt.getUserFromToken(token, utilisateurService);
        if(user == null || !user.isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }

        user.get().setPassword(null);
        return ResponseEntity.ok(user.get());
    }

    @ApiOperation("Récupère les types de comptes")
    @GetMapping("/user/types")
    public ResponseEntity<List<String>> getTypes() {
        List<String> lesRoles = roleService.findAll().stream()
                .map(Role::getLibelle)
                .filter(libelle -> !TypeCompteUtilisateur.Admin.getType().equals(libelle))
                .collect(Collectors.toList());
        return ResponseEntity.ok(lesRoles);
    }

    @ApiOperation("Permet d'ajouter un commentaire et une note à un restaurant")
    @ApiImplicitParam(name = "commentaire et note",
            value = "{\"commentaire\": \"string\", \"note\": integer}",
            required = true,
            dataType = "object",
            paramType = "body")
    @PostMapping("/user/comment/{idResteau}")
    public ResponseEntity addComment(@RequestHeader("Authorization") String token, @PathVariable("idResteau") Integer idResteau,
                                     @ApiParam(hidden = true) @RequestBody CommentaireCommercants commentaireCommercants) {
        Optional<Utilisateur> user = Jwt.getUserFromToken(token, utilisateurService);

        if(user == null || !user.isPresent()) {
            return ResponseEntity.badRequest().body("Utilisateur non connecté");
        }

        Utilisateur connectedUser = user.get();

        if(!TypeCompteUtilisateur.Client.getType().equals(connectedUser.getRole().getLibelle())) {
            return ResponseEntity.badRequest().body("Utilisateur non client");
        }

        Optional<Commercant> commercantOpt = commercantService.findById(idResteau);

        if(commercantOpt == null || !commercantOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Commercant non trouvé");
        }

        Commercant commercant = commercantOpt.get();

        commentaireCommercants.setUtilisateur(connectedUser);
        commentaireCommercants.setCommercant(commercant);

        //check if user has already commented
        boolean hasCommented = commentaireCommercantsService.existsByUtilisateurAndCommercant(connectedUser, commercant);

        if(hasCommented) {
            return ResponseEntity.badRequest().body("Vous avez déjà commenté ce restaurant");
        }

        //check if user has already ordered
        boolean hasOrdered = commandeService.findAllByUtilisateurId(connectedUser.getId()).stream()
            .anyMatch(commande ->
                commande.getProduitsCommander().stream()
                    .anyMatch(produitCommander ->
                        produitCommander.getProduit().getCommercant().getId().equals(commercant.getId())
                    )
        );

        if(!hasOrdered) {
            return ResponseEntity.badRequest().body("Vous n'avez pas commandé dans ce restaurant");
        }

        if(commentaireCommercants.getCommentaire() == null || commentaireCommercants.getCommentaire().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Commentaire vide");
        }

        if(commentaireCommercants.getNote() == null || commentaireCommercants.getNote() < 0 || commentaireCommercants.getNote() > 5) {
            return ResponseEntity.badRequest().body("Note invalide");
        }

        commentaireCommercantsService.save(commentaireCommercants);

        return ResponseEntity.ok("Commentaire ajouté");
    }
}
