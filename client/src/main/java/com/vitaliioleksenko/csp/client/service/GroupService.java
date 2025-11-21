package com.vitaliioleksenko.csp.client.service;

import com.vitaliioleksenko.csp.client.model.PageResponse;
import com.vitaliioleksenko.csp.client.model.group.GroupCreate;
import com.vitaliioleksenko.csp.client.model.group.GroupDetailed;
import com.vitaliioleksenko.csp.client.model.group.GroupPartial;
import com.vitaliioleksenko.csp.client.util.OkHttpClientFactory;
import okhttp3.*;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

public class GroupService {
    private static final String BASE_URL = "http://localhost:8080/api/group";
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public GroupService() {
        this.client = OkHttpClientFactory.getClient();
        this.objectMapper = new ObjectMapper();
    }

    public PageResponse<GroupPartial> getGroups(String search, Integer userId, Integer page, Integer size) throws IOException{
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();

        if (search != null) {
            urlBuilder.addQueryParameter("search", search);
        }
        if (userId != null) {
            urlBuilder.addQueryParameter("userId", userId.toString());
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
            if (!response.isSuccessful()) {
                throw new IOException("Server error: " + response.code());
            }
            JavaType type = objectMapper.getTypeFactory()
                    .constructParametricType(PageResponse.class, GroupPartial.class);
            return objectMapper.readValue(response.body().string(), type);
        }
    }

    public GroupDetailed getGroupById(int id) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + id)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if(response.code() == 404){
                throw new IOException("Wrong id");
            } else if (!response.isSuccessful()) {
                throw new IOException("Server error: " + response.code());
            }
            return objectMapper.readValue(response.body().string(), GroupDetailed.class);
        }
    }

    public void createGroup(GroupCreate groupCreate) throws IOException{
        String json = objectMapper.writeValueAsString(groupCreate);

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
                throw new IOException("Server error: " + response.code());
            }
        }
    }
}
