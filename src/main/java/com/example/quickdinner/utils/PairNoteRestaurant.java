package com.example.quickdinner.utils;

import com.example.quickdinner.model.Commercant;

public class PairNoteRestaurant {
    private Float note;
    private Commercant restaurant;

    public PairNoteRestaurant(Float note, Commercant restaurant) {
        this.note = note;
        this.restaurant = restaurant;
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
}
