package com.example.quickdinner.service;

import com.example.quickdinner.model.Commercant;

import java.util.List;
import java.util.Optional;

public interface CommercantService {
    List<Commercant> findAll();

    Optional<Commercant> findByUtilisateurId(int idUtilisateur);
}
