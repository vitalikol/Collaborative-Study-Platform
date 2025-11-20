package com.vitalioleksenko.csp.dto.membership;

import com.vitalioleksenko.csp.util.enums.GroupRole;
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
