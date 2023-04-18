package com.example.quickdinner.service;

import com.example.quickdinner.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Optional<Role> findByLibelle(String libelle);
    List<Role> findAll();
}
