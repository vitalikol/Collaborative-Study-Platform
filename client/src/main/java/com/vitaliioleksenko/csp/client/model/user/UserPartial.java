package com.vitaliioleksenko.csp.client.model.user;

import com.vitaliioleksenko.csp.client.util.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPartial {
    private int userId;
    private String name;
    private String email;
    private Role role;
}
