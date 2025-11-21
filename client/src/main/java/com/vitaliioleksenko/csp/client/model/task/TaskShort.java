package com.vitaliioleksenko.csp.client.model.task;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskShort {
    private int taskId;
    private String title;
}
