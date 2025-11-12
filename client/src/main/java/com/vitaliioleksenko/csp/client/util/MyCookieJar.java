package com.vitaliioleksenko.csp.client.util;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;


import java.util.ArrayList;
import java.util.List;

public class MyCookieJar implements CookieJar {
    private final List<Cookie> cookies = new ArrayList<>();
    long currentTime = System.currentTimeMillis();

    @Override
    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
        List<Cookie> validCookies = new ArrayList<>();

        for (Cookie cookie : cookies) {
            if (cookie.expiresAt() > currentTime && cookie.matches(httpUrl)) {
                validCookies.add(cookie);
            }
        }

        return validCookies;
    }

    @Override
    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> cookies) {
        this.cookies.clear();
        this.cookies.addAll(cookies);
    }
}
