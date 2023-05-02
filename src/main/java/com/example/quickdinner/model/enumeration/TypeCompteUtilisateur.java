package com.example.quickdinner.model.enumeration;

public enum TypeCompteUtilisateur {
    Client("Client"),
    Commercant("Commercant"),
    Admin("Admin");

    private String type;
    TypeCompteUtilisateur(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
