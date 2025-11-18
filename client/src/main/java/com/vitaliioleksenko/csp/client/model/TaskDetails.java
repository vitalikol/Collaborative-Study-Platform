package com.vitaliioleksenko.csp.client.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskDetails extends Task{
    private List<Resource> resources;

    public TaskDetails() {
        super();
    }
}
