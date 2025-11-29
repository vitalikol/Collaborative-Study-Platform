package com.vitaliioleksenko.csp.client.service;

import com.vitaliioleksenko.csp.client.model.membership.MembershipCreate;
import com.vitaliioleksenko.csp.client.model.membership.MembershipUpdate;
import com.vitaliioleksenko.csp.client.util.OkHttpClientFactory;
import okhttp3.*;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

public class MembershipService {
    private static final String BASE_URL = "http://localhost:8080/api/membership";
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public MembershipService() {
        this.client = OkHttpClientFactory.getClient();
        this.objectMapper = new ObjectMapper();
    }

    public void creatMembership(MembershipCreate membershipCreate) throws IOException{
        String json = objectMapper.writeValueAsString(membershipCreate);

        RequestBody body = RequestBody.create(
                json,
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.code() == 400) {
                throw new IOException(response.message());
            } else if (!response.isSuccessful()) {
                if (response.body().string().contains("UNIQUE constraint failed")) {
                    throw new IOException("User already in group");
                }
                throw new IOException("Server error: " + response.code());
            }
        }
    }

    public void editMembership(MembershipUpdate dto, int membershipId) throws IOException{
        String json = objectMapper.writeValueAsString(dto);

        RequestBody body = RequestBody.create(
                json,
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(BASE_URL + "/" + membershipId)
                .patch(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.code() == 400) {
                throw new IOException("Validation error " + response.message());
            } else if (!response.isSuccessful()) {
                throw new IOException("Server error: " + response.code());
            }
        }
    }

    public void deleteMembership(int membershipId) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + membershipId)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Server error: " + response.code());
            }
        }
    }
}
