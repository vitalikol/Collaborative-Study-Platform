package com.vitaliioleksenko.csp.client.util;

import com.vitaliioleksenko.csp.client.model.user.UserDetailed;
import com.vitaliioleksenko.csp.client.util.enums.Role;

public class UserSession {
    private static UserSession instance;
    private UserDetailed currentUser;

    private UserSession() {
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void login(UserDetailed user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
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
}