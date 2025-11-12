package com.vitaliioleksenko.csp.client.util;

import okhttp3.OkHttpClient;

public class OkHttpClientFactory {
    private static OkHttpClient client;

    public static OkHttpClient getClient() {
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .cookieJar(new MyCookieJar())
                    .build();
        }
        return client;
    }
}
