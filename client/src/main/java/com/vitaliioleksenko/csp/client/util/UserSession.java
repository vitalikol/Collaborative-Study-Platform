package com.vitaliioleksenko.csp.client.util;

import com.vitaliioleksenko.csp.client.model.User;

// Реалізація Singleton
public class UserSession {

    private static UserSession instance;

    private User currentUser;

    private UserSession() {
    }

    // Метод для отримання єдиного екземпляра
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    // Викликається при успішному логіні
    public void login(User user) {
        this.currentUser = user;
    }

    // Викликається при виході
    public void logout() {
        this.currentUser = null;
        // Тут також можна очистити кеш, повернути на сторінку логіна
    }

    // Методи для швидкого доступу з будь-якого контролера
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Integer getCurrentUserId() {
        return (currentUser != null) ? currentUser.getUserId() : null;
    }

    public String getCurrentUserRole() {
        return (currentUser != null) ? currentUser.getRole() : null;
    }
}