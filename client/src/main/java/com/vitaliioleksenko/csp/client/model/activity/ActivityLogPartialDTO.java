package com.vitaliioleksenko.csp.client.model.activity;

import com.vitaliioleksenko.csp.client.model.user.UserShort;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityLogPartialDTO {
    private int logId;
    private UserShort user;
    private String action;
    private LocalDateTime timestamp;
}
