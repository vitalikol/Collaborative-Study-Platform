package com.vitalioleksenko.csp.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Users")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;

    @Column(name = "name")
    @NotEmpty(message = "Name must not be empty")
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    @Column(name = "email")
    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Email must be valid")
    private String email;

    @Column(name = "password_hash")
    @NotEmpty(message = "Password must not be empty")
    private String passwordHash;
}