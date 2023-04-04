package com.example.quickdinner.controller;

import com.example.quickdinner.model.Utilisateur;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.quickdinner.service.UtilisateurService;

import java.util.Optional;

@Controller
@RequestMapping("/api")
public class UserController {

    private final UtilisateurService utilisateurService;

    public UserController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<Optional<Utilisateur>> getUser(@PathVariable String email){
        return ResponseEntity.ok(utilisateurService.findByEmail(email));
    }
}
