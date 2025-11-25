package com.vitalioleksenko.csp.models.dto.user;

import com.vitalioleksenko.csp.security.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDTO {
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    @Email(message = "Email must be valid")
    //@UniqueValue(message = "Email must be unique", fieldName = "email", entityClass = User.class)
    private String email;

    private String password;

    private Role role;
}
