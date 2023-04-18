package com.example.quickdinner.service;

import com.example.quickdinner.model.Utilisateur;

import java.util.Optional;

public interface UtilisateurService {
    Optional<Utilisateur> findByEmail(String email);

    void save(Utilisateur utilisateur);
    void deleteAdmin(Utilisateur utilisateur);
    void deleteClient(Utilisateur utilisateur);
    void deleteCommercant(Utilisateur utilisateur);
}
