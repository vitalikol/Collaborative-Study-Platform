package com.vitaliioleksenko.csp.client.util;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class JwtInterceptor implements Interceptor {

    private final UserSession session;

    public JwtInterceptor(UserSession session) {
        this.session = session;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        String token = session.getToken();

        if (token == null || token.isEmpty()) {
            return chain.proceed(original);
        }

        Request modified = original.newBuilder()
                .addHeader("Authorization", "Bearer " + token)
                .build();

        return chain.proceed(modified);
    }
}

