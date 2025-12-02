package com.vitalioleksenko.csp.services;

import com.vitalioleksenko.csp.models.dto.UserStats;
import com.vitalioleksenko.csp.models.dto.task.TaskCreateDTO;
import com.vitalioleksenko.csp.models.dto.task.TaskDetailedDTO;
import com.vitalioleksenko.csp.models.dto.task.TaskPartialDTO;
import com.vitalioleksenko.csp.models.dto.task.TaskUpdateDTO;
import com.vitalioleksenko.csp.models.Task;
import com.vitalioleksenko.csp.repositories.TasksRepository;
import com.vitalioleksenko.csp.services.notification.NotificationService;
import com.vitalioleksenko.csp.util.AppMapper;
import com.vitalioleksenko.csp.util.TaskSpecification;
import com.vitalioleksenko.csp.models.enums.TaskStatus;
import com.vitalioleksenko.csp.util.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TasksService {
    private final TasksRepository tasksRepository;
    private final NotificationService notificationService;
    private final AppMapper mapper;
    private final ActivitiesLogsService activitiesLogsService;

    @Autowired
    public TasksService(TasksRepository tasksRepository, NotificationService notificationService, @Qualifier("appMapperImpl") AppMapper mapper, ActivitiesLogsService activitiesLogsService) {
        this.tasksRepository = tasksRepository;
        this.notificationService = notificationService;
        this.mapper = mapper;
        this.activitiesLogsService = activitiesLogsService;
    }

    @Transactional
    public void save(TaskCreateDTO dto){
        Task task = mapper.toTask(dto);
        tasksRepository.save(task);
        notificationService.notifyTaskCreated(task);
        activitiesLogsService.log(
                "TASK_CREATED",
                "Created task with ID: " + task.getTaskId()
        );
    }

    public Page<TaskPartialDTO> getTasks(Integer groupId, Integer userId, TaskStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Task> result = tasksRepository.findAll(
                Specification.allOf(
                        TaskSpecification.visibleForUser(userId),
                        TaskSpecification.inGroup(groupId),
                        TaskSpecification.hasStatus(status)
                ),
                pageable
        );

        return result.map(mapper::toTaskPartial);
    }

    public Page<TaskPartialDTO> getActiveTasks(Integer groupId, Integer userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Task> result = tasksRepository.findAll(
                Specification.allOf(
                        TaskSpecification.visibleForUser(userId),
                        TaskSpecification.inGroup(groupId),
                        TaskSpecification.isActive()
                ),
                pageable
        );

        return result.map(mapper::toTaskPartial);
    }

    public TaskDetailedDTO getById(int id){
        Task task = tasksRepository.findById(id).orElseThrow(NotFoundException::new);
        return mapper.toTaskDetailed(task);
    }

    public UserStats getStatsForUser(int userId){
        UserStats stats = UserStats.builder()
                .doneTasks(tasksRepository.countTasksByStatus(userId, TaskStatus.DONE))
                .inReviewTasks(tasksRepository.countTasksByStatus(userId, TaskStatus.IN_REVIEW))
                .inProgressTasks(tasksRepository.countTasksByStatus(userId, TaskStatus.IN_PROGRESS))
                .build();

        return stats;
    }

    @Transactional
    public void edit(TaskUpdateDTO dto, int id){
        Task task = tasksRepository.findById(id).orElseThrow(NotFoundException::new);
        mapper.updateTaskFromDto(dto, task);
        tasksRepository.save(task);
        if (task.getStatus() == TaskStatus.DONE){
            notificationService.notifyTaskCompleted(task);
        } else {
            notificationService.notifyTaskUpdated(task);
        }
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
