package com.vitaliioleksenko.csp.client.model.membership;

import com.vitaliioleksenko.csp.client.model.group.GroupShort;
import com.vitaliioleksenko.csp.client.model.user.UserShort;
import com.vitaliioleksenko.csp.client.util.enums.GroupRole;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Membership {
    private int membershipId;
    private UserShort user;
    private GroupShort group;
    private GroupRole role;
    private LocalDateTime joinedAt;
}
