package com.vitalioleksenko.csp.dto.group;

import com.vitalioleksenko.csp.models.Membership;
import com.vitalioleksenko.csp.models.Resource;
import com.vitalioleksenko.csp.models.Task;
import com.vitalioleksenko.csp.models.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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
