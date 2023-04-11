package com.example.quickdinner.service;

import com.example.quickdinner.model.Commande;

import java.util.List;

public interface CommandeService {
    List<Commande> findAllByUtilisateurId(int idUtilisateur);
}
