package com.vitaliioleksenko.csp.client.model.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserShort {
    private int userId;
    private String email;
}
