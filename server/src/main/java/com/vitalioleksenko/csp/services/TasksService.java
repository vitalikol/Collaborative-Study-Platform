package com.vitalioleksenko.csp.services;

import com.vitalioleksenko.csp.dto.task.TaskCreateDTO;
import com.vitalioleksenko.csp.dto.task.TaskDetailedDTO;
import com.vitalioleksenko.csp.dto.task.TaskPartialDTO;
import com.vitalioleksenko.csp.dto.task.TaskUpdateDTO;
import com.vitalioleksenko.csp.models.Group;
import com.vitalioleksenko.csp.models.Task;
import com.vitalioleksenko.csp.models.User;
import com.vitalioleksenko.csp.repositories.GroupsRepository;
import com.vitalioleksenko.csp.repositories.TasksRepository;
import com.vitalioleksenko.csp.repositories.UsersRepository;
import com.vitalioleksenko.csp.util.AppMapper;
import com.vitalioleksenko.csp.util.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TasksService {
    private final TasksRepository tasksRepository;
    private final AppMapper mapper;
    private final ActivitiesLogsService activitiesLogsService;

    @Autowired
    public TasksService(TasksRepository tasksRepository, @Qualifier("appMapperImpl") AppMapper mapper, ActivitiesLogsService activitiesLogsService) {
        this.tasksRepository = tasksRepository;
        this.mapper = mapper;
        this.activitiesLogsService = activitiesLogsService;
    }

    @Transactional
    public void save(TaskCreateDTO dto){
        Task task = mapper.toTask(dto);
        Task saved = tasksRepository.save(task);
        mapper.toTaskDetailed(saved);
        activitiesLogsService.log(
                "TASK_CREATED",
                "Created task with ID: " + task.getTaskId()
        );
    }

    public Page<TaskPartialDTO> getTasks(Integer groupId, Integer userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> result;

        if (groupId != null && userId != null) {
            result = tasksRepository.findByUserIdAndGroupId(userId, groupId, pageable);
        } else if (groupId != null) {
            result = tasksRepository.findByGroup_GroupId(groupId, pageable);
        } else if (userId != null) {
            result = tasksRepository.findByUserId(userId, pageable);
        } else {
            result = tasksRepository.findAll(pageable);
        }

        return result.map(mapper::toTaskPartial);
    }

    public TaskDetailedDTO getById(int id){
        Task task = tasksRepository.findById(id).orElseThrow(NotFoundException::new);
        return mapper.toTaskDetailed(task);
    }

    @Transactional
    public void edit(TaskUpdateDTO dto, int id){
        Task task = tasksRepository.findById(id).orElseThrow(NotFoundException::new);
        mapper.updateTaskFromDto(dto, task);
        tasksRepository.save(task);
        activitiesLogsService.log(
                "TASK_EDITED",
                "Edited task with ID: " + task.getTaskId()
        );
    }

    @Transactional
    public boolean remove(int id){
        return tasksRepository.findById(id).map(task -> {
            tasksRepository.delete(task);
            activitiesLogsService.log(
                    "TASK_DELETED",
                    "Deleted task with id " + id
            );
            return true;
        }).orElse(false);
    }
}
