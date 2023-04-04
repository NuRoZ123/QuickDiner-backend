package com.example.quickdinner.controller;

import com.example.quickdinner.model.Utilisateur;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.quickdinner.service.UtilisateurService;

import java.util.Optional;

@Controller
@RequestMapping("/api")
public class UserController {

    private final UtilisateurService utilisateurService;

    public UserController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @PostMapping("/user/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> registerUser(@RequestBody Utilisateur utilisateur) {

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
        utilisateurService.save(utilisateur);
        return ResponseEntity.ok("User registered");
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<Optional<Utilisateur>> getUser(@PathVariable String email){
        return ResponseEntity.ok(utilisateurService.findByEmail(email));
    }
}
