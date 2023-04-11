package com.example.quickdinner.repository;

import com.example.quickdinner.model.Commercant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommercantRepository extends JpaRepository<Commercant, Integer> {
    Optional<Commercant> findByManagerId(int idUtilisateur);
}
