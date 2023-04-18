package com.example.quickdinner.repository;

import com.example.quickdinner.model.ProduitCommander;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProduitCommanderRepository extends JpaRepository<ProduitCommander, Integer> {
    List<ProduitCommander> findByProduitId(int idProduit);

    @Transactional
    @Modifying
    @Query("DELETE FROM ProduitCommander WHERE commande.id = :idCommande")
    void deleteAllByCommande(@Param("idCommande") int idCommande);

    @Transactional
    @Modifying
    @Query("DELETE FROM ProduitCommander WHERE produit.id = :idProduit")
    void deleteAllByProduit(@Param("idProduit") int idProduit);
}
