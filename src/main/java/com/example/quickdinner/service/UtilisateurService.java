package com.example.quickdinner.service;

import com.example.quickdinner.model.Utilisateur;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface UtilisateurService {
    Optional<Utilisateur> findByEmail(String email);
}
