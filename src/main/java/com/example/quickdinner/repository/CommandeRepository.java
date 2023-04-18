package com.example.quickdinner.repository;

import com.example.quickdinner.model.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Integer> {
    List<Commande> findAllByUtilisateurId(Integer idUtilisateur);

    @Transactional
    @Modifying
    @Query("delete from Commande where id = :idCommande")
    void delete(@Param("idCommande") Integer idCommande);

}
