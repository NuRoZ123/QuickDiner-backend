package com.example.quickdinner.controller;

import com.example.quickdinner.QuickDinnerApplication;
import com.example.quickdinner.model.Commande;
import com.example.quickdinner.model.Utilisateur;
import com.example.quickdinner.model.enumeration.TypeCompteUtilisateur;
import com.example.quickdinner.model.ws.WebsocketClient;
import com.example.quickdinner.service.UtilisateurService;
import com.example.quickdinner.utils.Jwt;
import com.example.quickdinner.utils.OberserveurWS;
import org.springframework.web.socket.*;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.Optional;

public class UserCommandeControllerWs implements WebSocketHandler, OberserveurWS {

    private final UtilisateurService utilisateurService;

    private final Hashtable<String, WebsocketClient> sessions = new Hashtable<>();

    public UserCommandeControllerWs(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;

        QuickDinnerApplication.playerCommandeObserver = this;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), new WebsocketClient(session));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("Error: " + exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        sessions.remove(session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
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

        if(!TypeCompteUtilisateur.Client.getType().equals(user.getRole().getLibelle())) {
            session.sendMessage(new TextMessage("{\"Error\": \"Vous n'avez pas les droits pour accéder à cette ressource\"}"));
            return;
        }

        sessions.get(session.getId()).setUtilisateurId(user.getId());
        session.sendMessage(new TextMessage("{\"Success\": \"Vous êtes connecté en tant que " + user.getNom() + "\"}"));
    }

    @Override
    public void update(Commande commande) {
        sessions.forEach((id, websocketClient) -> {
            if(websocketClient.getUtilisateurId() <= 0) {
                return;
            }

            if(commande.getUtilisateur().getId() != websocketClient.getUtilisateurId()) {
                return;
            }

            try {
                websocketClient.getSession().sendMessage(new TextMessage("{\"message\": \"Votre commande " + commande.getId() + " est prête\", \"id\": " + commande.getId() + "}"));
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }
}
