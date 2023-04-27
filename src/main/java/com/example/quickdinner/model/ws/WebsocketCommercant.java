package com.example.quickdinner.model.ws;

import org.springframework.web.socket.WebSocketSession;

public class WebsocketCommercant {
    private WebSocketSession session;
    private int idRestaurant;
    public WebsocketCommercant(WebSocketSession session) {
        this.session = session;
        this.idRestaurant = -1;
    }

    public void setIdRestaurant(int idRestaurant) {
        this.idRestaurant = idRestaurant;
    }

    public int getIdRestaurant() {
        return this.idRestaurant;
    }

    public WebSocketSession getSession() {
        return this.session;
    }
}
