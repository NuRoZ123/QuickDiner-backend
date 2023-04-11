package com.example.quickdinner.controller;

import com.example.quickdinner.model.Commercant;
import com.example.quickdinner.model.Utilisateur;
import com.example.quickdinner.service.CommercantService;
import com.example.quickdinner.service.RoleService;
import com.example.quickdinner.service.UtilisateurService;
import com.example.quickdinner.utils.Jwt;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api")
public class CommercantController {

    private final CommercantService commercantService;

    public CommercantController(CommercantService commercantService) {
        this.commercantService = commercantService;
    }

    @ApiOperation("Get all commercants (restaurant)")
    @GetMapping("/restaurants")
    public ResponseEntity<List<Commercant>> register() {
       return ResponseEntity.ok(commercantService.findAll());
    }
}
