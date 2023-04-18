package com.example.quickdinner.repository;

import com.example.quickdinner.model.ProduitPanier;
import com.example.quickdinner.model.ProduitPanierId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProduitPanierRepository extends JpaRepository<ProduitPanier, ProduitPanierId> {

    @Transactional
    @Modifying
    @Query("DELETE FROM ProduitPanier WHERE panier.id = :panierId AND produit.id = :produitId")
    void delete(@Param("panierId") int panierId, @Param("produitId") int produitId);

    @Transactional
    @Modifying
    @Query("DELETE FROM ProduitPanier WHERE panier.id = :panierId")
    void deleteAllByPanier(@Param("panierId") int panierId);
}