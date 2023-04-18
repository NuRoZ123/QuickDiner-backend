package com.example.quickdinner.utils;

import com.example.quickdinner.model.Commercant;
import com.example.quickdinner.model.Utilisateur;

public class TripleUtilisateurCommercantType {
    private Utilisateur utilisateur;
    private Commercant restaurant;

    private String type;

    public TripleUtilisateurCommercantType(Utilisateur utilisateur, Commercant restaurant, String type) {
        this.utilisateur = utilisateur;
        this.restaurant = restaurant;
        this.type = type;
    }

    public Utilisateur getUtilisateur() {
        return this.utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Commercant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Commercant restaurant) {
        this.restaurant = restaurant;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
