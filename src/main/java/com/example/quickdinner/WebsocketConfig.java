package com.example.quickdinner;

import com.example.quickdinner.controller.CommandeControllerWs;
import com.example.quickdinner.controller.UserCommandeControllerWs;
import com.example.quickdinner.service.CommercantService;
import com.example.quickdinner.service.ProduitCommanderService;
import com.example.quickdinner.service.UtilisateurService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@ComponentScan("com.example.quickdinner")
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

    private final UtilisateurService utilisateurService;
    private final CommercantService commercantService;
    private final ProduitCommanderService produitCommanderService;

    public WebsocketConfig(UtilisateurService utilisateurService, CommercantService commercantService,
                           ProduitCommanderService produitCommanderService) {
        this.utilisateurService = utilisateurService;
        this.commercantService = commercantService;
        this.produitCommanderService = produitCommanderService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new CommandeControllerWs(utilisateurService, commercantService, produitCommanderService),
                "/websocket/restaurants/commandes").setAllowedOrigins("*");

        registry.addHandler(new UserCommandeControllerWs(utilisateurService),
                "/websocket/user/commandes").setAllowedOrigins("*");
    }
}
