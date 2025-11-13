package com.vitaliioleksenko.csp.client.service;


import com.vitaliioleksenko.csp.client.model.LoginRequest;

import com.vitaliioleksenko.csp.client.model.RegisterRequest;
import com.vitaliioleksenko.csp.client.model.User;
import com.vitaliioleksenko.csp.client.util.OkHttpClientFactory;
import okhttp3.*;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

public class AuthService {
    private static final String BASE_URL = "http://localhost:8080/auth";
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public AuthService() {
        this.client = OkHttpClientFactory.getClient();
        this.objectMapper = new ObjectMapper();
    }

    public void login(LoginRequest loginRequest) throws IOException {
        String json = objectMapper.writeValueAsString(loginRequest);

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
                throw new IOException("Invalid credentials");
            } else if (!response.isSuccessful()) {
                throw new IOException("Server error: " + response.code());
            }

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
                throw new IOException(response.message());
            } else if (!response.isSuccessful()) {
                throw new IOException("Server error: " + response.code());
            }
        }
    }

    public User me() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/me")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.code() == 403) {
                throw new IOException("Forbidden");
            } else if (!response.isSuccessful()) {
                throw new IOException("Server error: " + response.code());
            }
            String json = response.body().string();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, User.class);
        }
    }
}
