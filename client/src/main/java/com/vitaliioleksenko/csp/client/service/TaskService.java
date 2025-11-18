package com.vitaliioleksenko.csp.client.service;

import com.vitaliioleksenko.csp.client.model.Task;
import com.vitaliioleksenko.csp.client.model.TaskDetails;
import com.vitaliioleksenko.csp.client.util.OkHttpClientFactory;
import okhttp3.*;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class TaskService {
    private static final String BASE_URL = "http://localhost:8080/api/task";
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public TaskService() {
        this.client = OkHttpClientFactory.getClient();
        this.objectMapper = new ObjectMapper();
    }

    public TaskDetails getTaskById(int id) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + id)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if(response.code() == 404){
                throw new IOException("Wrong id");
            } else if (!response.isSuccessful()) {
                throw new IOException("Server error: " + response.code());
            }
            return objectMapper.readValue(response.body().string(), TaskDetails.class);
        }
    }


    public List<Task> getTasks(Integer userId, Integer teamId) throws IOException{
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();

        if (teamId != null) {
            urlBuilder.addQueryParameter("groupId", teamId.toString());
        }
        if (userId != null) {
            urlBuilder.addQueryParameter("userId", userId.toString());
        }

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if(response.code() == 403){
                throw new IOException("First log in");
            } else if (!response.isSuccessful()) {
                throw new IOException("Server error: " + response.code());
            }
            JavaType type = objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, Task.class);
            return objectMapper.readValue(response.body().string(), type);
        }
    }
}
