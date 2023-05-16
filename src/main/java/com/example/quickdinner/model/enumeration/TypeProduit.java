package com.example.quickdinner.model.enumeration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TypeProduit {
    ENTREE("ENTREE"),
    PLAT("PLAT"),
    DESSERT("DESSERT"),
    PETITE_FAIM("P'TITE FAIM"),
    MENU("MENU"),
    PETIT_DEJEUNER("P'TIT DEJ"),
    BOISSON("BOISSON");

    public static List<TypeProduit> getAsList() {
        return Arrays.stream(TypeProduit.values()).collect(Collectors.toList());
    }

    public static TypeProduit getByString(String typeProduit) {
        return Arrays.stream(TypeProduit.values())
                .filter(typeProduitEnum -> typeProduitEnum.getTypeProduit().equals(typeProduit))
                .findFirst()
                .orElse(null);
    }

    private String typeProduit;

    TypeProduit(String typeProduit) {
        this.typeProduit = typeProduit;
    }

    public String getTypeProduit() {
        return typeProduit;
    }
}
