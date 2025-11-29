package com.vitaliioleksenko.csp.client.util;

import com.sun.net.httpserver.HttpServer;
import lombok.Getter;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;

public class OAuthCallbackServer {

    private HttpServer server;
    @Getter
    private String token;

    public void start(Runnable onSuccess) {
        try {
            server = HttpServer.create(new InetSocketAddress(8085), 0);

            server.createContext("/oauth2/callback", exchange -> {
                URI requestURI = exchange.getRequestURI();
                String query = requestURI.getQuery();

                if (query != null && query.startsWith("token=")) {
                    token = query.substring("token=".length());

                    String responseText = "Google login successful! You can close this window.";
                    exchange.sendResponseHeaders(200, responseText.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(responseText.getBytes());
                    os.close();

                    onSuccess.run();
                }
            });

            server.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }
}

