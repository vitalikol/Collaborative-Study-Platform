package com.vitaliioleksenko.csp.client.service;

import com.vitaliioleksenko.csp.client.model.Task;
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

    public Task getTaskById(int id) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + id)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if(response.code() == 404){
                throw new IOException("Wrong id");
            } else if (!response.isSuccessful()) {
                throw new IOException("Server error: " + response.code());
            }
            return objectMapper.readValue(response.body().string(), Task.class);
        }
    }

    public List<Task> getMyTasks() throws IOException{
        Request request = new Request.Builder()
                .url(BASE_URL + "/my")
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
