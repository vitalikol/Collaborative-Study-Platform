package com.vitalioleksenko.csp.models.dto.membership;
import com.vitalioleksenko.csp.models.enums.GroupRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembershipUpdateDTO {
    private GroupRole role;
}
