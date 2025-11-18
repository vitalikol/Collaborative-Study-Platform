package com.vitaliioleksenko.csp.client.service;

import com.vitaliioleksenko.csp.client.model.Group;
import com.vitaliioleksenko.csp.client.model.Task;
import com.vitaliioleksenko.csp.client.util.OkHttpClientFactory;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class GroupService {
    private static final String BASE_URL = "http://localhost:8080/api/group";
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public GroupService() {
        this.client = OkHttpClientFactory.getClient();
        this.objectMapper = new ObjectMapper();
    }

    public Group getGroupById(int id) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + id)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if(response.code() == 404){
                throw new IOException("Wrong id");
            } else if (!response.isSuccessful()) {
                throw new IOException("Server error: " + response.code());
            }
            return objectMapper.readValue(response.body().string(), Group.class);
        }
    }

    public List<Group> getGroups(Integer userId) throws IOException{
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        if (userId != null) {
            urlBuilder.addQueryParameter("userId", userId.toString());
        }
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();

        try(Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Server error: " + response.code());
            }
            JavaType type = objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, Group.class);
            return objectMapper.readValue(response.body().string(), type);
        }
    }
}
