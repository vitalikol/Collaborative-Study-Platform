package com.vitalioleksenko.csp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "Resources")
public class Resource {
    @Id
    @Column(name = "resource_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int resourceId;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "uploaded_by", referencedColumnName = "user_id")
    private User user;

    @Column(name = "title")
    @NotEmpty(message = "Title must not be empty")
    @Size(min = 5, message = "Title must be at least 5 characters long")
    private String title;

    @Column(name = "type")
    @NotEmpty(message = "Type must not be empty")
    private String type;

    @Column(name = "path_or_url")
    @NotEmpty(message = "Path or url must not be empty")
    private String pathOrUrl;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPathOrUrl() {
        return pathOrUrl;
    }

    public void setPathOrUrl(String pathOrUrl) {
        this.pathOrUrl = pathOrUrl;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
