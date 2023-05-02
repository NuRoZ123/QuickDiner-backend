package com.example.quickdinner.controller;

import com.example.quickdinner.QuickDinnerApplication;
import com.example.quickdinner.model.Commande;
import com.example.quickdinner.model.Commercant;
import com.example.quickdinner.model.ProduitCommander;
import com.example.quickdinner.model.Utilisateur;
import com.example.quickdinner.model.ws.WebsocketCommercant;
import com.example.quickdinner.service.CommandeService;
import com.example.quickdinner.service.CommercantService;
import com.example.quickdinner.service.ProduitCommanderService;
import com.example.quickdinner.service.UtilisateurService;
import com.example.quickdinner.utils.Jwt;
import com.example.quickdinner.utils.OberserveurWS;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;

@Component
public class CommandeControllerWs implements WebSocketHandler, OberserveurWS {

    private final UtilisateurService utilisateurService;
    private final CommercantService commercantService;
    private final ProduitCommanderService produitCommanderService;
    private final CommandeService commandeService;

    private final Hashtable<String, WebsocketCommercant> sessions = new Hashtable<>();

    public CommandeControllerWs(UtilisateurService utilisateurService, CommercantService commercantService,
                                ProduitCommanderService produitCommanderService, CommandeService commandeService) {
        this.utilisateurService = utilisateurService;
        this.commercantService = commercantService;
        this.produitCommanderService = produitCommanderService;
        this.commandeService = commandeService;

        QuickDinnerApplication.commandeQueuObserver = this;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.put(session.getId(), new WebsocketCommercant(session));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        System.out.println("Error: " + exception.getMessage());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws IOException {
        JsonReader jsonReader = Json.createReader(new StringReader(message.getPayload().toString()));

        JsonObject jsonObject;

        try {
            jsonObject = jsonReader.readObject();
        } catch (Exception e) {
            session.sendMessage(new TextMessage("{\"Error\": \"invalid json\"}"));
            return;
        }

        if(!jsonObject.containsKey("token")) {
            session.sendMessage(new TextMessage("{\"Error\": \"token is required\"}"));
            return;
        }

        Optional<Utilisateur> userOpt = Jwt.getUserFromToken(jsonObject.getString("token"), utilisateurService);

        if(userOpt == null || !userOpt.isPresent()) {
            session.sendMessage(new TextMessage("{\"Error\": \"invalid token\"}"));
            return;
        }

        Utilisateur user = userOpt.get();

        if(!"Commercant".equals(user.getRole().getLibelle())) {
            session.sendMessage(new TextMessage("{\"Error\": \"Vous n'avez pas les droits pour accéder à cette ressource\"}"));
            return;
        }

        Optional<Commercant> commercantOpt = commercantService.findByUtilisateurId(user.getId());

        if(!commercantOpt.isPresent()) {
            session.sendMessage(new TextMessage("{\"Error\": \"Erreur restaurant inconnue pour ce compter merci de vous référer a un admin.\"}"));
            return;
        }

        Commercant commercant = commercantOpt.get();

        sessions.get(session.getId()).setIdRestaurant(commercant.getId());
        session.sendMessage(new TextMessage("{\"Success\": \"Vous êtes connecté en tant que " + commercant.getNom() + "\"}"));
    }

    @Override
    public void update() {
        sessions.forEach((id, websocketCommercant) -> {
            if(websocketCommercant.getIdRestaurant() <= 0) {
                return;
            }

            // copy la liste
            (new ArrayList<>(QuickDinnerApplication.commandesQueue)).forEach(idCommande -> {
                List<ProduitCommander> produitCommanders = produitCommanderService.findAllByCommandeId(idCommande);
                Commande commandePourLeResteau = new Commande(0, new ArrayList<>(), null);
                commandePourLeResteau.setId(idCommande);

                produitCommanders.forEach(produitCommander -> {
                    Optional<Commercant> commercantOpt = commercantService.findByProduitId(produitCommander.getProduit().getId());

                    if(commercantOpt == null || !commercantOpt.isPresent()) {
                        return;
                    }

                    Commercant commercant = commercantOpt.get();

                    if(commercant.getId() != websocketCommercant.getIdRestaurant()) {
                        return;
                    }

                    commandePourLeResteau.getProduitsCommander().add(produitCommander);
                });

                if(commandePourLeResteau.getProduitsCommander().size() == 0) {
                    return;
                }

                try {
                    websocketCommercant.getSession().sendMessage(new TextMessage(commandePourLeResteau.toJson()));
                } catch (IOException e) {
                    System.out.println("Error: " + e.getMessage());
                    throw new RuntimeException(e);
                }
            });
        });

        QuickDinnerApplication.commandesQueue.clear();

    }
}
