package com.example.quickdinner.service;

import com.example.quickdinner.model.Commercant;
import com.example.quickdinner.model.Utilisateur;

import java.util.List;
import java.util.Optional;

public interface CommercantService {
    List<Commercant> findAll();
    Optional<Commercant> findById(int id);
    Optional<Commercant> findByUtilisateurId(int idUtilisateur);

    Optional<Commercant> findByProduitId(int idProduit);
    void deleteByManager(Utilisateur manager);

    void save(Commercant commercant);
}
