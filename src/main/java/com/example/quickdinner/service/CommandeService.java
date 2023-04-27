package com.example.quickdinner.service;

import com.example.quickdinner.model.Commande;

import java.util.List;
import java.util.Optional;

public interface CommandeService {
    List<Commande> findAllByUtilisateurId(int idUtilisateur);
    Commande save(Commande commande);
    void delete(Commande commande);

    Optional<Commande> findById(int id);
}
