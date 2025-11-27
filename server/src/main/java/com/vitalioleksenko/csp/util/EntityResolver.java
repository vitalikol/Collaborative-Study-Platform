package com.vitalioleksenko.csp.util;

import com.vitalioleksenko.csp.models.Group;
import com.vitalioleksenko.csp.models.Task;
import com.vitalioleksenko.csp.models.User;
import com.vitalioleksenko.csp.repositories.GroupsRepository;
import com.vitalioleksenko.csp.repositories.TasksRepository;
import com.vitalioleksenko.csp.repositories.UsersRepository;
import com.vitalioleksenko.csp.util.exceptions.NotFoundException;
import org.springframework.stereotype.Component;

@Component
public class EntityResolver {

    private final UsersRepository usersRepository;
    private final GroupsRepository groupsRepository;
    private final TasksRepository tasksRepository;

    public EntityResolver(UsersRepository usersRepository,
                          GroupsRepository groupsRepository,
                          TasksRepository tasksRepository) {
        this.usersRepository = usersRepository;
        this.groupsRepository = groupsRepository;
        this.tasksRepository = tasksRepository;
    }

    public Group getGroup(Integer id) {
        if (id == null) return null;
        return groupsRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    public User getUser(Integer id) {
        if (id == null) return null;
        return usersRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    public Task getTask(Integer id) {
        if (id == null) return null;
        return tasksRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }
}
