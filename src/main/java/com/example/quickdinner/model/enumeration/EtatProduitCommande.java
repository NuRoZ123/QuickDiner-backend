package com.example.quickdinner.model.enumeration;

public enum EtatProduitCommande {
    WIP("En cours"),
    DONE("Terminée");

    private String libelle;
    EtatProduitCommande(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
