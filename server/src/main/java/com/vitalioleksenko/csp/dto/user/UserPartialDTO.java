package com.vitalioleksenko.csp.dto.user;

import com.vitalioleksenko.csp.security.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPartialDTO {
    private int userId;

    @NotEmpty(message = "Name must not be empty")
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Email must be valid")
    //@UniqueValue(message = "Email must be unique", fieldName = "email", entityClass = User.class)
    private String email;

    private Role role;

}
