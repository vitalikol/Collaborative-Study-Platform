package com.vitalioleksenko.csp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Activity_Log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityLog {
    @Id
    @Column(name = "log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int logId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "action")
    @NotEmpty(message = "Action must not be empty")
    private String action;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "details")
    private String details;
}
