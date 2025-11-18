package com.vitalioleksenko.csp.dto.activity;

import com.vitalioleksenko.csp.dto.user.UserShortDTO;
import com.vitalioleksenko.csp.models.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityLogPartialDTO {
    private int logId;

    private UserShortDTO user;

    @NotEmpty(message = "Action must not be empty")
    private String action;

    private LocalDateTime timestamp;
}
