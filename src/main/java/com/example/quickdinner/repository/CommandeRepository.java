package com.example.quickdinner.repository;

import com.example.quickdinner.model.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Integer> {
    List<Commande> findAllByUtilisateurId(Integer idUtilisateur);

}
