package com.vitalioleksenko.csp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;

@Entity
@Table(name = "Memberships")
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

    @NotEmpty(message = "Role must not be empty")
    @Column(name = "role")
    private String role;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    public Membership() {}

    public Membership(int membershipId, User user, Group group, String role, LocalDateTime joinedAt) {
        this.membershipId = membershipId;
        this.user = user;
        this.group = group;
        this.role = role;
        this.joinedAt = joinedAt;
    }

    public int getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(int membershipId) {
        this.membershipId = membershipId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }
}
