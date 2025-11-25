package com.vitalioleksenko.csp.models.dto.group;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupShortDTO {
    private int groupId;

    @NotEmpty(message = "Name must not be empty")
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;
}
