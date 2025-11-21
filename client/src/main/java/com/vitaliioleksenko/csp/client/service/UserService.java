package com.vitaliioleksenko.csp.client.service;

import com.vitaliioleksenko.csp.client.model.PageResponse;
import com.vitaliioleksenko.csp.client.model.user.UserDetailed;
import com.vitaliioleksenko.csp.client.model.user.UserPartial;
import com.vitaliioleksenko.csp.client.util.OkHttpClientFactory;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

public class UserService {
    private static final String BASE_URL = "http://localhost:8080/api/user";
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public UserService() {
        this.client = OkHttpClientFactory.getClient();
        this.objectMapper = new ObjectMapper();
    }

    public UserDetailed getUserById(int id) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + id)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if(response.code() == 404){
                throw new IOException("Wrong id");
            } else if (!response.isSuccessful()) {
                throw new IOException("Server error: " + response.code());
            }
            return objectMapper.readValue(response.body().string(), UserDetailed.class);
        }
    }

    public PageResponse<UserPartial> getUsers(String search, Integer page, Integer size) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();

        if (search != null) {
            urlBuilder.addQueryParameter("search", search);
        }
        if (page != null) {
            urlBuilder.addQueryParameter("page", page.toString());
        }
        if (size != null) {
            urlBuilder.addQueryParameter("size", size.toString());
        }

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();

        try(Response response = client.newCall(request).execute()) {
            if(response.code() == 403){
                throw new IOException("First log in");
            } else if (!response.isSuccessful()) {
                throw new IOException("Server error: " + response.code());
            }
            JavaType type = objectMapper.getTypeFactory()
                    .constructParametricType(PageResponse.class, UserPartial.class);
            return objectMapper.readValue(response.body().string(), type);
        }
    }
}
