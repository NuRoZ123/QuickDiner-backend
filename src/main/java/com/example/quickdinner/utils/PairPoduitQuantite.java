package com.example.quickdinner.utils;

public class PairPoduitQuantite {
    private int id;
    private int quantite;

    public PairPoduitQuantite(int id, int quantite) {
        this.id = id;
        this.quantite = quantite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
}
