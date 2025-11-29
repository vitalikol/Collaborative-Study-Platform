package com.vitaliioleksenko.csp.client.util;

import com.vitaliioleksenko.csp.client.model.user.UserDetailed;
import com.vitaliioleksenko.csp.client.util.enums.Role;

public class UserSession {

    private static UserSession instance;

    private String jwtToken;
    private UserDetailed currentUser;

    private UserSession() {}

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setToken(String token) {
        this.jwtToken = token;
    }

    public String getToken() {
        return jwtToken;
    }

    public boolean hasToken() {
        return jwtToken != null && !jwtToken.isEmpty();
    }

    public void setCurrentUser(UserDetailed user) {
        this.currentUser = user;
    }

    public UserDetailed getCurrentUser() {
        return currentUser;
    }

    public Integer getCurrentUserId() {
        return (currentUser != null) ? currentUser.getUserId() : null;
    }

    public Role getCurrentUserRole() {
        return (currentUser != null) ? currentUser.getRole() : null;
    }

    // ------- SESSION CONTROL -------
    public boolean isLoggedIn() {
        return hasToken();
    }

    public void logout() {
        this.jwtToken = null;
        this.currentUser = null;
    }
}
