package com.example.quickdinner.service.impl;

import com.example.quickdinner.model.Produit;
import com.example.quickdinner.repository.ProduitRepository;
import com.example.quickdinner.service.ProduitService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log
public class ProduitServiceImpl implements ProduitService {

    private ProduitRepository produitRepository;
    ProduitServiceImpl(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    @Override
    public List<Produit> findAllByCommercant(int idCommercant) {
        return produitRepository.findAllByCommercantId(idCommercant);
    }

    @Override
    public Optional<Produit> findById(int id) {
        return produitRepository.findById(id);
    }
}
