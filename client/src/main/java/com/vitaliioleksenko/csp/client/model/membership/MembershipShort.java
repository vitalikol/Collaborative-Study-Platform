package com.vitaliioleksenko.csp.client.model.membership;

import com.vitaliioleksenko.csp.client.model.user.UserShort;
import com.vitaliioleksenko.csp.client.util.enums.GroupRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembershipShort {
    private int membershipId;
    private UserShort user;
    private GroupRole role;
}
