package com.vitaliioleksenko.csp.client.service;

import com.vitaliioleksenko.csp.client.model.ErrorResponse;
import com.vitaliioleksenko.csp.client.model.JwtResponse;
import com.vitaliioleksenko.csp.client.model.user.AuthenticationRequest;
import com.vitaliioleksenko.csp.client.model.user.RegisterRequest;
import com.vitaliioleksenko.csp.client.model.user.UserDetailed;
import com.vitaliioleksenko.csp.client.util.ErrorMessageParser;
import com.vitaliioleksenko.csp.client.util.OkHttpClientFactory;
import com.vitaliioleksenko.csp.client.util.UserSession;
import okhttp3.*;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

public class AuthService {
    private static final String BASE_URL = "http://localhost:8080/api/auth";
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public AuthService() {
        this.client = OkHttpClientFactory.getClient();
        this.objectMapper = new ObjectMapper();
    }

    public void login(AuthenticationRequest authenticationRequest) throws IOException {
        String json = objectMapper.writeValueAsString(authenticationRequest);

        RequestBody body = RequestBody.create(
                json,
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(BASE_URL + "/login")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.code() == 404) {
                String errorJson = response.body().string();
                ErrorResponse errorResponse = objectMapper.readValue(errorJson, ErrorResponse.class);
                throw new IOException(ErrorMessageParser.extractFirstMessage(errorResponse.getMessage()));
            } else if (!response.isSuccessful()) {
                throw new IOException("Server error: " + response.code());
            }
            JwtResponse jwtResponse = objectMapper.readValue(response.body().string(), JwtResponse.class);
            UserSession session = UserSession.getInstance();
            session.setToken(jwtResponse.getToken());
            session.setCurrentUser(me());
        }
    }

    public void register(RegisterRequest registerRequest) throws IOException{
        String json = objectMapper.writeValueAsString(registerRequest);

        RequestBody body = RequestBody.create(
                json,
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(BASE_URL + "/register")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.code() == 400) {
                String errorJson = response.body().string();
                ErrorResponse errorResponse = objectMapper.readValue(errorJson, ErrorResponse.class);
                throw new IOException(ErrorMessageParser.extractFirstMessage(errorResponse.getMessage()));
            } else if (!response.isSuccessful()) {
                throw new IOException("Server error: " + response.code());
            }
        }
    }

    public void logOut() throws IOException{
        RequestBody body = RequestBody.EMPTY;

        Request request = new Request.Builder()
                .url(BASE_URL + "/logout")
                .post(body)
                .build();

        client.newCall(request).execute();
    }

    public UserDetailed me() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/me")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.code() == 403) {
                throw new IOException("First login");
            } else if (!response.isSuccessful()) {
                throw new IOException("Server error: " + response.code());
            }
            String json = response.body().string();
            return objectMapper.readValue(json, UserDetailed.class);
        }
    }
}
