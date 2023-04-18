package com.example.quickdinner.service;

import com.example.quickdinner.model.ProduitCommander;

import java.util.List;

public interface ProduitCommanderService {
    List<ProduitCommander> findAllByProduitId(int idProduit);
    List<ProduitCommander> saveAll(List<ProduitCommander> produitCommanders);
}
