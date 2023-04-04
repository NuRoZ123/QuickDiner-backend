package com.example.quickdinner.repository;

import com.example.quickdinner.model.Role;
import com.example.quickdinner.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByLibelle(String libelle);
}
