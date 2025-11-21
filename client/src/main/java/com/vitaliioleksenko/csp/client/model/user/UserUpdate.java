package com.vitaliioleksenko.csp.client.model.user;

import com.vitaliioleksenko.csp.client.util.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdate {
    private String name;
    private String email;
    private String password;
    private Role role;
}
