package com.vitalioleksenko.csp.config;

import com.vitalioleksenko.csp.security.UserIdHandshakeInterceptor;
import com.vitalioleksenko.csp.websocket.MessageHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final MessageHandler messageHandler;

    public WebSocketConfig(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        registry.addHandler(messageHandler, "/ws/notifications")
                .addInterceptors(new HttpSessionHandshakeInterceptor(), new UserIdHandshakeInterceptor())
                .setAllowedOrigins("*");
    }
}
