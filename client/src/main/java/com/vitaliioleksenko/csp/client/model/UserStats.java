package com.vitaliioleksenko.csp.client.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStats {
    private int doneTasks;
    private int inReviewTasks;
    private int inProgressTasks;
}
