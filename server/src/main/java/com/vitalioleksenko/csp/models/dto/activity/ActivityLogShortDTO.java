package com.vitalioleksenko.csp.models.dto.activity;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

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
