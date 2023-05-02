package com.example.quickdinner.service;

import com.example.quickdinner.model.CommentaireCommercants;
import com.example.quickdinner.model.Commercant;
import com.example.quickdinner.model.Utilisateur;

public interface CommentaireCommercantsService {
    Float findNote(int idCommercant);
    void deleteAllByUtilisateur(Utilisateur utilisateur);
    void deleteAllByCommercant(Commercant commercant);

    boolean existsByUtilisateurAndCommercant(Utilisateur utilisateur, Commercant commercant);

    CommentaireCommercants save(CommentaireCommercants commentaireCommercants);
}
