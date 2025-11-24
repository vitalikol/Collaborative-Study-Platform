package com.vitaliioleksenko.csp.client.service;

import com.vitaliioleksenko.csp.client.model.PageResponse;
import com.vitaliioleksenko.csp.client.model.resource.ResourceCreate;
import com.vitaliioleksenko.csp.client.model.resource.ResourcePartial;
import com.vitaliioleksenko.csp.client.model.resource.ResourceShort;
import com.vitaliioleksenko.csp.client.model.task.TaskCreate;
import com.vitaliioleksenko.csp.client.model.task.TaskDetailed;
import com.vitaliioleksenko.csp.client.model.user.UserPartial;
import com.vitaliioleksenko.csp.client.util.OkHttpClientFactory;
import okhttp3.*;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ResourceService {
    private static final String BASE_URL = "http://localhost:8080/api/resource";
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public ResourceService() {
        this.client = OkHttpClientFactory.getClient();
        this.objectMapper = new ObjectMapper();
    }

    public PageResponse<ResourcePartial> getResources(Integer taskId, Integer page, Integer size) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();

        if (taskId != null) {
            urlBuilder.addQueryParameter("search", taskId.toString());
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
                    .constructParametricType(PageResponse.class, ResourcePartial.class);
            return objectMapper.readValue(response.body().string(), type);
        }
    }

    public ResourceShort createResource(ResourceCreate resourceCreate) throws IOException{
        String json = objectMapper.writeValueAsString(resourceCreate);

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
            return objectMapper.readValue(response.body().string(), ResourceShort.class);
        }
    }

    public void uploadResource(File file, int resourceId) throws IOException{
        RequestBody fileBody = RequestBody.create(
                file,
                MediaType.parse("application/json; charset=utf-8")
        );

        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + "/" + resourceId + "/file")
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Server error: " + response.code());
            }
        }
    }
}
