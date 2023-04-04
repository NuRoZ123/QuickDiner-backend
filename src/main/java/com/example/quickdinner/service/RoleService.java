package com.example.quickdinner.service;

import com.example.quickdinner.model.Role;
import com.example.quickdinner.model.Utilisateur;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findByLibelle(String libelle);
}
