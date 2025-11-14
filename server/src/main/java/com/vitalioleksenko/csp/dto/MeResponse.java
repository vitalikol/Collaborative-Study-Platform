package com.vitalioleksenko.csp.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeResponse {
    private int id;
    private String email;
}
