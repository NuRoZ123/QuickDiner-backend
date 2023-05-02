package com.example.quickdinner.repository;

import com.example.quickdinner.model.CommentaireCommercants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentaireCommercantsRepository extends JpaRepository<CommentaireCommercants, Integer> {

    List<CommentaireCommercants> findByCommercantId(int idCommercant);

    @Transactional
    @Modifying
    @Query("delete from CommentaireCommercants where utilisateur.id = :idUtilisateur")
    void deleteAllByUtilisateur(@Param("idUtilisateur") int idUtilisateur);

    @Transactional
    @Modifying
    @Query("delete from CommentaireCommercants where commercant.id = :idCommercant")
    void deleteAllByCommercant(@Param("idCommercant") int idCommercant);

    Optional<CommentaireCommercants> findByCommercantIdAndUtilisateurId(int idCommercant, int idUtilisateur);
}
