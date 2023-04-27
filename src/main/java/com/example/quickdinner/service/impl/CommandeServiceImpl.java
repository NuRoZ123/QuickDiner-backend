package com.example.quickdinner.service.impl;

import com.example.quickdinner.model.Commande;
import com.example.quickdinner.repository.CommandeRepository;
import com.example.quickdinner.service.CommandeService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public void delete(Commande commande) {
        commandeRepository.delete(commande.getId());
    }

    @Override
    public Optional<Commande> findById(int id) {
        return commandeRepository.findById(id);
    }
}
