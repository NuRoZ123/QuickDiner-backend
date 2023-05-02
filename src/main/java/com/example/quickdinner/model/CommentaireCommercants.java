package com.example.quickdinner.model;


import lombok.*;

import javax.persistence.*;

@Builder
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
