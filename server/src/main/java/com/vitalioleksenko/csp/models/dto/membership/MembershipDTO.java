package com.vitalioleksenko.csp.models.dto.membership;

import com.vitalioleksenko.csp.models.dto.group.GroupShortDTO;
import com.vitalioleksenko.csp.models.dto.user.UserShortDTO;
import com.vitalioleksenko.csp.models.enums.GroupRole;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Role must not be empty")
    private GroupRole role;

    private LocalDateTime joinedAt;
}
