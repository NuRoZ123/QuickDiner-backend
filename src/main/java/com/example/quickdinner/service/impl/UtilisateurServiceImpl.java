package com.example.quickdinner.service.impl;

import com.example.quickdinner.model.Utilisateur;
import com.example.quickdinner.repository.UtilisateurRepository;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import com.example.quickdinner.service.UtilisateurService;

import java.util.Optional;

@Service
@Log
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public Optional<Utilisateur> findByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }
}
