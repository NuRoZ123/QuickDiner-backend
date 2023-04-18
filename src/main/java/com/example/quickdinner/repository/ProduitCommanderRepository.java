package com.example.quickdinner.repository;

import com.example.quickdinner.model.ProduitCommander;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProduitCommanderRepository extends JpaRepository<ProduitCommander, Integer> {
    List<ProduitCommander> findByProduitId(int idProduit);
}
