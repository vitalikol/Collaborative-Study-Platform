package com.vitalioleksenko.csp.dto.membership;

import com.vitalioleksenko.csp.models.Group;
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
public class MembershipShortDTO {
    private int membershipId;

    @NotEmpty(message = "Role must not be empty")
    private String role;
}
