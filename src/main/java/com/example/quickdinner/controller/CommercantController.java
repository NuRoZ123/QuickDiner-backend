package com.example.quickdinner.controller;

import com.example.quickdinner.service.CommentaireCommercantsService;
import com.example.quickdinner.service.CommercantService;
import com.example.quickdinner.utils.PairNoteRestaurant;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api")
public class CommercantController {

    private final CommercantService commercantService;
    private final CommentaireCommercantsService commentaireCommercantsService;

    public CommercantController(CommercantService commercantService,
            CommentaireCommercantsService commentaireCommercantsService) {
        this.commercantService = commercantService;
        this.commentaireCommercantsService = commentaireCommercantsService;
    }

    @ApiOperation(value = "Get all commercants (restaurant)",
    response = List.class)
    @GetMapping("/restaurants")
    public ResponseEntity<List<PairNoteRestaurant>> register() {
        List<PairNoteRestaurant> commercants = new ArrayList<>();

        commercantService.findAll().forEach(commercant -> {

            Float note = commentaireCommercantsService.findNote(commercant.getId());

            commercants.add(new PairNoteRestaurant(note, commercant));
        });

        return ResponseEntity.ok(commercants);
    }
}
