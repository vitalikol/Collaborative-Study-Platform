package com.vitaliioleksenko.csp.client.model.membership;

import com.vitaliioleksenko.csp.client.util.enums.GroupRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembershipUpdate {
    private GroupRole role;
}
