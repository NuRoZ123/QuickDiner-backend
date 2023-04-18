package com.example.quickdinner.repository;

import com.example.quickdinner.model.Commercant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommercantRepository extends JpaRepository<Commercant, Integer> {
    Optional<Commercant> findByManagerId(int idUtilisateur);
}
