package com.vitaliioleksenko.csp.client.websocket;


import com.vitaliioleksenko.csp.client.model.NotificationMessage;
import com.vitaliioleksenko.csp.client.util.UserSession;
import lombok.Setter;
import okhttp3.*;
import tools.jackson.databind.ObjectMapper;

import java.util.function.Consumer;

public class NotificationWebSocket {

    private WebSocket socket;
    private final OkHttpClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    @Setter private Consumer<NotificationMessage> messageHandler;

    @Setter private String jwtToken;

    public NotificationWebSocket(OkHttpClient client) {
        this.jwtToken = UserSession.getInstance().getToken();
        this.client = client;
    }

    public void connect() {
        if (jwtToken == null) {
            throw new IllegalStateException("JWT token must be set before connecting WebSocket");
        }

        String wsUrl = "ws://csp-app.xyz:8080/ws/notifications?token=" + jwtToken;

        Request request = new Request.Builder()
                .url(wsUrl)
                .build();

        socket = client.newWebSocket(request, new WebSocketListener() {

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                try {
                    NotificationMessage msg = mapper.readValue(text, NotificationMessage.class);
                    if (messageHandler != null) {
                        messageHandler.accept(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                System.out.println("WS FAILED → Reconnecting in 3s");
                reconnect();
            }
        });
    }

    public void reconnect() {
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                connect();
            } catch (Exception ignored) {}
        }).start();
    }

    public void close() {
        if (socket != null) socket.close(1000, "Client closed");
    }
}
