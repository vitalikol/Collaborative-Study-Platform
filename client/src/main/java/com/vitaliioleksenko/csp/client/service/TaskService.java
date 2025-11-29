package com.vitaliioleksenko.csp.client.service;

import com.vitaliioleksenko.csp.client.model.PageResponse;
import com.vitaliioleksenko.csp.client.model.task.TaskCreate;
import com.vitaliioleksenko.csp.client.model.task.TaskDetailed;
import com.vitaliioleksenko.csp.client.model.task.TaskPartial;
import com.vitaliioleksenko.csp.client.model.task.TaskUpdate;
import com.vitaliioleksenko.csp.client.util.OkHttpClientFactory;
import com.vitaliioleksenko.csp.client.util.enums.TaskStatus;
import okhttp3.*;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

public class TaskService {
    private static final String BASE_URL = "http://localhost:8080/api/task";
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public TaskService() {
        this.client = OkHttpClientFactory.getClient();
        this.objectMapper = new ObjectMapper();
    }

    public TaskDetailed getTaskById(int id) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + id)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if(response.code() == 404){
                throw new IOException("Wrong id");
            } else if (!response.isSuccessful()) {
                throw new IOException("Server error: " + response.code());
            }
            return objectMapper.readValue(response.body().string(), TaskDetailed.class);
        }
    }

    public PageResponse<TaskPartial> getActiveTasks(Integer userId, Integer teamId, Integer page, Integer size) throws IOException{
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "/active").newBuilder();

        if (teamId != null) {
            urlBuilder.addQueryParameter("groupId", teamId.toString());
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

        try (Response response = client.newCall(request).execute()) {
            if(response.code() == 403){
                throw new IOException("First log in");
            } else if (!response.isSuccessful()) {
                throw new IOException("Server error: " + response.code());
            }
            JavaType type = objectMapper.getTypeFactory()
                    .constructParametricType(PageResponse.class, TaskPartial.class);
            return objectMapper.readValue(response.body().string(), type);
        }
    }

    public PageResponse<TaskPartial> getDoneTasks(Integer userId, Integer teamId, Integer page, Integer size) throws IOException{
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();

        if (teamId != null) {
            urlBuilder.addQueryParameter("groupId", teamId.toString());
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
        urlBuilder.addQueryParameter("status", TaskStatus.DONE.toString());


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
                    .constructParametricType(PageResponse.class, TaskPartial.class);
            return objectMapper.readValue(response.body().string(), type);
        }
    }

    public void createTask(TaskCreate taskCreate) throws IOException{
        String json = objectMapper.writeValueAsString(taskCreate);

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

    public void editTask(TaskUpdate dto, int taskId) throws IOException{
        String json = objectMapper.writeValueAsString(dto);

        RequestBody body = RequestBody.create(
                json,
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(BASE_URL + "/" + taskId)
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

    public void deleteTask(int taskId) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + taskId)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Server error: " + response.code());
            }
        }
    }
}
