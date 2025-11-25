package com.vitalioleksenko.csp.models.dto.membership;

import com.vitalioleksenko.csp.models.enums.GroupRole;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembershipCreateDTO {
    private Integer userId;

    private Integer groupId;

    @NotNull(message = "Role must not be empty")
    private GroupRole role;
}
