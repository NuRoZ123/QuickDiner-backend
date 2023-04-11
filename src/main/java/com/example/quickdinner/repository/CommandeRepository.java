package com.example.quickdinner.repository;

import com.example.quickdinner.model.Commande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Integer> {
    List<Commande> findAllByUtilisateurId(Integer idUtilisateur);

}
