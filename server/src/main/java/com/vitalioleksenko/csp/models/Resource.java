package com.vitalioleksenko.csp.models;

import com.vitalioleksenko.csp.models.enums.ResourceFormat;
import com.vitalioleksenko.csp.models.enums.ResourceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Resources")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resource {
    @Id
    @Column(name = "resource_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int resourceId;

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "task_id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "uploaded_by", referencedColumnName = "user_id")
    private User user;

    @Column(name = "title")
    @NotEmpty(message = "Title must not be empty")
    @Size(min = 5, message = "Title must be at least 5 characters long")
    private String title;

    @Column(name = "type")
    @NotNull(message = "Type must not be empty")
    @Enumerated(EnumType.STRING)
    private ResourceType type;

    @Column(name = "format")
    @NotNull(message = "Format must not be empty")
    @Enumerated(EnumType.STRING)
    private ResourceFormat format;

    @Column(name = "path_or_url")
    private String pathOrUrl;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;
}
