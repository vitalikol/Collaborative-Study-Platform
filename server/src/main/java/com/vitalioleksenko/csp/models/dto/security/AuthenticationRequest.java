package com.vitalioleksenko.csp.models.dto.security;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationRequest {
    private String username;
    private String password;
}
