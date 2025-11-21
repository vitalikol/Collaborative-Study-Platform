package com.vitalioleksenko.csp.dto.membership;

import com.vitalioleksenko.csp.dto.user.UserShortDTO;
import com.vitalioleksenko.csp.util.enums.GroupRole;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembershipShortDTO {
    private int membershipId;

    private UserShortDTO user;

    @NotNull(message = "Role must not be empty")
    private GroupRole role;
}
