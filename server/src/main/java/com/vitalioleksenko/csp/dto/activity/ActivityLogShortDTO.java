package com.vitalioleksenko.csp.dto.activity;

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
public class ActivityLogShortDTO {
    private int logId;

    @Column(name = "action")
    @NotEmpty(message = "Action must not be empty")
    private String action;
}
