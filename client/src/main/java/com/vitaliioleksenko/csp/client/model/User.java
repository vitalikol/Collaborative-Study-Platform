package com.vitaliioleksenko.csp.client.model;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
public class User {
    private int userId;
    private String name;
    private String email;
    private String role;
}
