package com.example.quickdinner.service.impl;

import com.example.quickdinner.model.Panier;
import com.example.quickdinner.repository.PanierRepository;
import com.example.quickdinner.service.PanierService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

@Service
@Log
public class PanierServiceImpl implements PanierService {

    private final PanierRepository panierRepository;

    public PanierServiceImpl(PanierRepository panierRepository) {
        this.panierRepository = panierRepository;
    }

    @Override
    public Panier save(Panier panier) {
        return panierRepository.save(panier);
    }

    @Override
    public void delete(Panier panier) {
        panierRepository.delete(panier);
    }
}
