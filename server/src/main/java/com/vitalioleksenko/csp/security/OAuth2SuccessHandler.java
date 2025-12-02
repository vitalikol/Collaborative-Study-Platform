package com.vitalioleksenko.csp.security;

import com.vitalioleksenko.csp.models.dto.security.CustomOAuthUser;
import com.vitalioleksenko.csp.services.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Autowired
    public OAuth2SuccessHandler(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        CustomOAuthUser user = (CustomOAuthUser) authentication.getPrincipal();

        String jwt = jwtService.generateToken(user.getUserId(), user.getEmail());

        response.sendRedirect("http://localhost:8085/oauth2/callback?token=" + jwt);
    }
}
