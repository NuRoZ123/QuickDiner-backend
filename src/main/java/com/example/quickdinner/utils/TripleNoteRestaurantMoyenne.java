package com.example.quickdinner.utils;

import com.example.quickdinner.model.Commercant;

public class TripleNoteRestaurantMoyenne {
    private Float note;
    private Commercant restaurant;
    private Float prixMoyen;

    public TripleNoteRestaurantMoyenne(Float note, Commercant restaurant, Float prixMoyen) {
        this.note = note;
        this.restaurant = restaurant;
        this.prixMoyen = prixMoyen;
    }

    public Float getNote() {
        return ((float) Math.round(note * 100)) / 100;
    }

    public void setNote(Float note) {
        this.note = note;
    }

    public Commercant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Commercant restaurant) {
        this.restaurant = restaurant;
    }

    public Float getPrixMoyen() {
        return ((float) Math.round(prixMoyen * 100)) / 100;
    }

    public void setPrixMoyen(Float moyenne) {
        this.prixMoyen = moyenne;
    }
}
