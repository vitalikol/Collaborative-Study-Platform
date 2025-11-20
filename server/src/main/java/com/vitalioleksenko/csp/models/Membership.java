package com.vitalioleksenko.csp.models;

import com.vitalioleksenko.csp.util.enums.GroupRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Memberships")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Membership {
    @Id
    @Column(name = "membership_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int membershipId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "group_id")
    private Group group;

    @NotNull(message = "Role must not be empty")
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private GroupRole role;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;
}
