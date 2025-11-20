package com.vitalioleksenko.csp.dto.group;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupUpdateDTO {
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    @Size(min = 40, message = "Description must be at least 40 characters long")
    private String description;
}
