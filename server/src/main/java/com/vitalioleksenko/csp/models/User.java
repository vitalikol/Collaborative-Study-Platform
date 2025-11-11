package com.vitalioleksenko.csp.models;

import com.vitalioleksenko.csp.validation.UniqueValue;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;


@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @OneToMany(mappedBy = "createdBy")
    private List<Group> createdGroups;

    @OneToMany(mappedBy = "user")
    private List<Membership> memberships;

    @OneToMany(mappedBy = "user")
    private List<Task> tasks;

    @OneToMany(mappedBy = "user")
    private List<Resource> resources;

    @OneToMany(mappedBy = "user")
    private List<ActivityLog> activities;

    @Column(name = "name")
    @NotEmpty(message = "Name must not be empty")
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    @Column(name = "email")
    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Email must be valid")
    @UniqueValue(message = "Email must be unique", fieldName = "email", entityClass = User.class)
    private String email;

    @Column(name = "password_hash")
    @NotEmpty(message = "Password must not be empty")
    private String passwordHash;
}