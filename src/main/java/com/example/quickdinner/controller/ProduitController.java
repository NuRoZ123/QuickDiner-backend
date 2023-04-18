package com.example.quickdinner.controller;

import com.example.quickdinner.model.Produit;
import com.example.quickdinner.service.ProduitService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api")
@CrossOrigin(origins = {"*"})
public class ProduitController {

    private ProduitService produitService;

    ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    @ApiOperation(value = "Get all produits (menu) by restaurant",
            response = List.class)
    @GetMapping("/produits/{idRestaurant}")
    public ResponseEntity<List<Produit>> findAllProduitsByRestaurant(@PathVariable Integer idRestaurant) {
        return ResponseEntity.ok(produitService.findAllByCommercant(idRestaurant));
    }
}
