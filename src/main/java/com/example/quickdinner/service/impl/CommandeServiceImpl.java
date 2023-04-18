package com.example.quickdinner.service.impl;

import com.example.quickdinner.model.Commande;
import com.example.quickdinner.repository.CommandeRepository;
import com.example.quickdinner.service.CommandeService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
public class CommandeServiceImpl implements CommandeService {

    private final CommandeRepository commandeRepository;
    CommandeServiceImpl(CommandeRepository commandeRepository) {
        this.commandeRepository = commandeRepository;
    }

    @Override
    public List<Commande> findAllByUtilisateurId(int idUtilisateur) {
        return commandeRepository.findAllByUtilisateurId(idUtilisateur);
    }

    @Override
    public Commande save(Commande commande) {
        return commandeRepository.save(commande);
    }
}
