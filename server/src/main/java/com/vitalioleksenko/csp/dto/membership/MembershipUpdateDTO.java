package com.vitalioleksenko.csp.dto.membership;
import com.vitalioleksenko.csp.util.enums.GroupRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembershipUpdateDTO {
    private GroupRole role;
}
