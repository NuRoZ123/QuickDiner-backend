package com.example.quickdinner.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentaireCommercantsId implements Serializable {

    private Integer utilisateur;
    private Integer commercant;
}
