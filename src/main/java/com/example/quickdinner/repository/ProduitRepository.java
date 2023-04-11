package com.example.quickdinner.repository;

import com.example.quickdinner.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit, Integer> {

    List<Produit> findAllByCommercantId(Integer idCommercant);
}
