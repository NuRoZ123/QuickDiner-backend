package com.example.quickdinner.service.impl;

import com.example.quickdinner.model.ProduitCommander;
import com.example.quickdinner.repository.ProduitCommanderRepository;
import com.example.quickdinner.service.ProduitCommanderService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
public class ProduitCommanderServiceImpl implements ProduitCommanderService {

    private final ProduitCommanderRepository produitCommanderRepository;

    ProduitCommanderServiceImpl(ProduitCommanderRepository produitCommanderRepository) {
        this.produitCommanderRepository = produitCommanderRepository;
    }

    @Override
    public List<ProduitCommander> findAllByProduitId(int idProduit) {
        return produitCommanderRepository.findByProduitId(idProduit);
    }
}
