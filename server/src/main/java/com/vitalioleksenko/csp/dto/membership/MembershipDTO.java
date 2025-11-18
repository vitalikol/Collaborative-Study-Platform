package com.vitalioleksenko.csp.dto.membership;

import com.vitalioleksenko.csp.dto.group.GroupShortDTO;
import com.vitalioleksenko.csp.dto.user.UserShortDTO;
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
public class MembershipDTO {
    private int membershipId;

    private UserShortDTO user;

    private GroupShortDTO group;

    @NotEmpty(message = "Role must not be empty")
    private String role;

    private LocalDateTime joinedAt;
}
