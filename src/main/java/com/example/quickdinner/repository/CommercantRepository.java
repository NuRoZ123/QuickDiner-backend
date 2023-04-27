package com.example.quickdinner.repository;

import com.example.quickdinner.model.Commercant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CommercantRepository extends JpaRepository<Commercant, Integer> {
    Optional<Commercant> findByManagerId(int idUtilisateur);

//    @Transactional
    // SELECT c.id, c.nom  from commercants c inner join produits p on c.id = p.commercant_id where p.id = 1;
    @Query("SELECT c from Commercant c inner join Produit p on c.id = p.commercant.id where p.id = :idProduit")
    Optional<Commercant> findByProduitsId(@Param("idProduit") int idProduit);

    @Transactional
    @Modifying
    @Query("delete from Commercant where manager.id = :manager")
    void deleteByManager(@Param("manager") int manager);
}
