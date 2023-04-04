package com.example.quickdinner.service.impl;

import com.example.quickdinner.model.Role;
import com.example.quickdinner.model.Utilisateur;
import com.example.quickdinner.repository.RoleRepository;
import com.example.quickdinner.repository.UtilisateurRepository;
import com.example.quickdinner.service.RoleService;
import com.example.quickdinner.service.UtilisateurService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<Role> findByLibelle(String libelle) {
        return roleRepository.findByLibelle(libelle);
    }
}
