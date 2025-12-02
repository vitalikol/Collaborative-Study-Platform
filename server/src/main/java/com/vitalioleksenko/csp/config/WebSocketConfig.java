package com.vitalioleksenko.csp.config;

import com.vitalioleksenko.csp.security.JwtHandshakeInterceptor;
import com.vitalioleksenko.csp.services.security.JwtService;
import com.vitalioleksenko.csp.websocket.MessageHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final MessageHandler messageHandler;
    private final JwtService jwtService;

    public WebSocketConfig(MessageHandler messageHandler, JwtService jwtService) {
        this.messageHandler = messageHandler;
        this.jwtService = jwtService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        registry.addHandler(messageHandler, "/ws/notifications")
                .addInterceptors(new JwtHandshakeInterceptor(jwtService))
                .setAllowedOrigins("*");
    }
}
