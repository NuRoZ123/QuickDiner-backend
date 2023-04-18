package com.example.quickdinner.service;

import com.example.quickdinner.model.Commande;

import java.util.List;

public interface CommandeService {
    List<Commande> findAllByUtilisateurId(int idUtilisateur);
    Commande save(Commande commande);
    void delete(Commande commande);
}
