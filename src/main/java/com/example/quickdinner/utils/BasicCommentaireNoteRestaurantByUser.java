package com.example.quickdinner.utils;

public class BasicCommentaireNoteRestaurantByUser {

    private String nomUtilisateur;
    private int idCommercant;
    private String commentaire;
    private float note;

    public BasicCommentaireNoteRestaurantByUser(String nomUtilisateur, int idCommercant, String commentaire, float note) {
        this.nomUtilisateur = nomUtilisateur;
        this.idCommercant = idCommercant;
        this.commentaire = commentaire;
        this.note = note;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public int getIdCommercant() {
        return idCommercant;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public float getNote() {
        return note;
    }
}
