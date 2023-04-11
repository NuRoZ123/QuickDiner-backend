package com.example.quickdinner.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

@Table(name = "commantaire_commercants")
@IdClass(CommentaireCommercantsId.class)
public class CommentaireCommercants {

    @Id
    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @Id
    @ManyToOne
    @JoinColumn(name = "commercant_id")
    private Commercant commercant;

    private String commentaire;
    private Integer note;
}
