package com.example.quickdinner.model.ws;

import org.springframework.web.socket.WebSocketSession;

public class WebsocketClient {
    private WebSocketSession session;
    private int utilisateurId;

    public WebsocketClient(WebSocketSession session) {
        this.session = session;
        this.utilisateurId = -1;
    }

    public void setUtilisateurId(int utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public int getUtilisateurId() {
        return utilisateurId;
    }

    public WebSocketSession getSession() {
        return session;
    }
}
