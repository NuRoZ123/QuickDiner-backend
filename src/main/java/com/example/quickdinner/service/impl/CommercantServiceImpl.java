package com.example.quickdinner.service.impl;

import com.example.quickdinner.model.Commercant;
import com.example.quickdinner.model.Utilisateur;
import com.example.quickdinner.repository.CommercantRepository;
import com.example.quickdinner.service.CommercantService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log
public class CommercantServiceImpl implements CommercantService {

    private final CommercantRepository commercantRepository;
    public CommercantServiceImpl(CommercantRepository commercantRepository) {
        this.commercantRepository = commercantRepository;
    }

    @Override
    public List<Commercant> findAll() {
        return this.commercantRepository.findAll();
    }

    @Override
    public Optional<Commercant> findById(int id) {
        return this.commercantRepository.findById(id);
    }

    @Override
    public Optional<Commercant> findByUtilisateurId(int idUtilisateur) {
        return this.commercantRepository.findByManagerId(idUtilisateur);
    }

    @Override
    public void deleteByManager(Utilisateur manager) {
        this.commercantRepository.deleteByManager(manager.getId());
    }

    @Override
    public void save(Commercant commercant) {
        this.commercantRepository.save(commercant);
    }
}
