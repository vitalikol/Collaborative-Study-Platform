package com.vitaliioleksenko.csp.client.model.activity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityLogShort {
    private int logId;
    private String action;
}
