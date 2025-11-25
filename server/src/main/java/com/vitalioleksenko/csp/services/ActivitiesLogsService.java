package com.vitalioleksenko.csp.services;

import com.vitalioleksenko.csp.dto.activity.ActivityLogDetailedDTO;
import com.vitalioleksenko.csp.models.ActivityLog;
import com.vitalioleksenko.csp.models.User;
import com.vitalioleksenko.csp.repositories.ActivitiesLogsRepository;
import com.vitalioleksenko.csp.repositories.UsersRepository;
import com.vitalioleksenko.csp.security.CustomUserDetails;
import com.vitalioleksenko.csp.util.AppMapper;
import com.vitalioleksenko.csp.util.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class ActivitiesLogsService {
    private final ActivitiesLogsRepository activitiesLogsRepository;
    private final UsersRepository usersRepository;
    private final AppMapper mapper;

    @Autowired
    public ActivitiesLogsService(ActivitiesLogsRepository activitiesLogsTaskRepository, UsersRepository usersRepository, @Qualifier("appMapperImpl") AppMapper mapper) {
        this.activitiesLogsRepository = activitiesLogsTaskRepository;
        this.usersRepository = usersRepository;
        this.mapper = mapper;
    }

    @Transactional
    public void log(String action, String details) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            ActivityLog log = new ActivityLog();
            log.setUser(null);
            log.setAction(action);
            log.setDetails(details + " (anonymous)");
            log.setTimestamp(LocalDateTime.now());
            activitiesLogsRepository.save(log);
            return;
        }
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User user = usersRepository.findById(userDetails.getId()).orElseThrow(NotFoundException::new);

        ActivityLog log = new ActivityLog();
        log.setUser(user);
        log.setAction(action);
        log.setDetails(details + " by user with ID: " + user.getUserId());
        log.setTimestamp(LocalDateTime.now());

        activitiesLogsRepository.save(log);
    }

    public Page<ActivityLogDetailedDTO> findAll(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return activitiesLogsRepository.findAll(pageable).map(mapper::toActivityLogDetailed);
    }

    public Page<ActivityLogDetailedDTO> findAllForUser(int userId, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return activitiesLogsRepository.findByUser_UserId(userId, pageable).map(mapper::toActivityLogDetailed);
    }

    public ActivityLogDetailedDTO findById(int id){
        ActivityLog log = activitiesLogsRepository.findById(id).orElseThrow(NotFoundException::new);
        return mapper.toActivityLogDetailed(log);
    }
}
