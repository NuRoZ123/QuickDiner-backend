package com.example.quickdinner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Commandes")
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ProduitCommander> produitsCommander;

    @ManyToOne
    @JsonIgnore
    private Utilisateur utilisateur;

    public String toJson() {
        StringBuilder json = new StringBuilder("{\"id\":" + id + ",\"produitsCommander\":[");

        StringBuilder jsonProduitCommander = json;
        produitsCommander.forEach(produitCommander -> {
            jsonProduitCommander.append(produitCommander.toJson() + ",");
        });

        jsonProduitCommander.deleteCharAt(jsonProduitCommander.length() - 1);

        json.append("]}");
        return json.toString();
    }
}
