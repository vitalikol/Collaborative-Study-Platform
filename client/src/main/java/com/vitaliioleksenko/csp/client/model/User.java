package com.vitaliioleksenko.csp.client.model;

// model/User.java
public class User {
    private Long userId;
    private String name;
    private String email;

    // Конструктори, гетери, сетери
    public User() {}

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Гетери та сетери
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
