package com.vitaliioleksenko.csp.client.model.activity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityLogShortDTO {
    private int logId;
    private String action;
}
