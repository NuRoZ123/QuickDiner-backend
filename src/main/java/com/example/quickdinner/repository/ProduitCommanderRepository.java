package com.example.quickdinner.repository;

import com.example.quickdinner.model.ProduitCommander;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProduitCommanderRepository extends JpaRepository<ProduitCommander, Integer> {
    List<ProduitCommander> findByProduitId(int idProduit);
}
