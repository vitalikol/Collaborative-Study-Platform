package com.vitaliioleksenko.csp.client.service;

import com.vitaliioleksenko.csp.client.model.PageResponse;
import com.vitaliioleksenko.csp.client.model.activity.ActivityLogDetailed;
import com.vitaliioleksenko.csp.client.util.OkHttpClientFactory;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

public class LogService {
    private static final String BASE_URL = "http://138.199.153.164:8080/api/log";
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public LogService() {
        this.client = OkHttpClientFactory.getClient();
        this.objectMapper = new ObjectMapper();
    }

    public PageResponse<ActivityLogDetailed> getLogs(Integer userId, Integer page, Integer size) throws IOException {
        HttpUrl.Builder urlBuilder;

        if (userId != null){
            urlBuilder = HttpUrl.parse(BASE_URL + "/user/" + userId).newBuilder();
        } else {
            urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
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
                    .constructParametricType(PageResponse.class, ActivityLogDetailed.class);
            return objectMapper.readValue(response.body().string(), type);
        }
    }
}
