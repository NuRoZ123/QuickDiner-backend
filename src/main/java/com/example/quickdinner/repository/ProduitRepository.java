package com.example.quickdinner.repository;

import com.example.quickdinner.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Integer> {
    List<Produit> findAllByCommercantId(Integer idCommercant);

    @Transactional
    @Modifying
    @Query("DELETE FROM Produit WHERE id = :idProduit")
    void delete(@Param("idProduit") int idProduit);
}
