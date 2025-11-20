package com.vitalioleksenko.csp.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserShortDTO {
    private int userId;

    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Email must be valid")
    //@UniqueValue(message = "Email must be unique", fieldName = "email", entityClass = User.class)
    private String email;
}
