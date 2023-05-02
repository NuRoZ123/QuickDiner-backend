package com.example.quickdinner.model.ws;

import org.springframework.web.socket.WebSocketSession;

public class WebsocketClient {
    private WebSocketSession session;
    private int utilisateur;

    public WebsocketClient(WebSocketSession session) {
        this.session = session;
        this.utilisateur = -1;
    }

    public void setUtilisateur(int utilisateur) {
        this.utilisateur = utilisateur;
    }

    public int getUtilisateur() {
        return utilisateur;
    }

    public WebSocketSession getSession() {
        return session;
    }
}
