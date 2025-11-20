package com.vitalioleksenko.csp.dto.activity;

import com.vitalioleksenko.csp.dto.user.UserShortDTO;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityLogDetailedDTO {
    private int logId;

    private UserShortDTO user;

    @NotEmpty(message = "Action must not be empty")
    private String action;

    private LocalDateTime timestamp;

    private String details;
}
