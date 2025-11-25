package com.vitalioleksenko.csp.models.dto.user;

import com.vitalioleksenko.csp.models.User;
import com.vitalioleksenko.csp.validation.UniqueValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Email must be valid")
    @UniqueValue(message = "Email must be unique", fieldName = "email", entityClass = User.class)
    private String email;

    @NotEmpty(message = "Password must not be empty")
    private String password;

    @NotEmpty(message = "Name must not be empty")
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;
}

