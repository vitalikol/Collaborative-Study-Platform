package com.vitalioleksenko.csp.security;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class UserIdHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {

        if (request instanceof ServletServerHttpRequest servletRequest) {

            HttpSession httpSession = servletRequest.getServletRequest().getSession(false);

            if (httpSession != null) {

                Object ctx = httpSession.getAttribute("SPRING_SECURITY_CONTEXT");

                if (ctx instanceof SecurityContext securityContext) {
                    Authentication auth = securityContext.getAuthentication();

                    if (auth != null && auth.getPrincipal() instanceof CustomUserDetails user) {
                        attributes.put("userId", user.getId());
                    }
                }
            }
        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {}
}


