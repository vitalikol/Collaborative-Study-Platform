package com.vitalioleksenko.csp.services;

import com.vitalioleksenko.csp.models.Task;
import com.vitalioleksenko.csp.repositories.TasksRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeadlineCheckerService {

    private final TasksRepository tasksRepository;
    private final NotificationService notificationService;

    public DeadlineCheckerService(TasksRepository tasksRepository,
                                  NotificationService notificationService) {
        this.tasksRepository = tasksRepository;
        this.notificationService = notificationService;
    }

    @Scheduled(fixedRate = 3600000)
    public void checkDeadlines() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime soon = now.plusHours(24);

        List<Task> tasks = tasksRepository.findTasksWithDeadlineBetween(now, soon);

        for (Task task : tasks) {
            notificationService.notifyDeadlineSoon(task);
        }
    }
}
