package com.vitaliioleksenko.csp.client.util;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpClientFactory {
    private static OkHttpClient client;

    public static OkHttpClient getClient() {
        if (client == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            client = new OkHttpClient.Builder()
                    .addInterceptor(new JwtInterceptor(UserSession.getInstance()))
                    .addInterceptor(loggingInterceptor)
                    .build();
        }
        return client;
    }
}
