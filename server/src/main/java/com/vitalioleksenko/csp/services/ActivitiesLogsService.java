package com.vitalioleksenko.csp.services;

import com.vitalioleksenko.csp.models.ActivityLog;
import com.vitalioleksenko.csp.models.User;
import com.vitalioleksenko.csp.repositories.ActivitiesLogsRepository;
import com.vitalioleksenko.csp.repositories.UsersRepository;
import com.vitalioleksenko.csp.security.CustomUserDetails;
import com.vitalioleksenko.csp.util.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ActivitiesLogsService {
    private final ActivitiesLogsRepository activitiesLogsRepository;
    private final ModelMapper modelMapper;
    private final UsersRepository usersRepository;

    @Autowired
    public ActivitiesLogsService(ActivitiesLogsRepository activitiesLogsTaskRepository, ModelMapper modelMapper, UsersRepository usersRepository) {
        this.activitiesLogsRepository = activitiesLogsTaskRepository;
        this.modelMapper = modelMapper;
        this.usersRepository = usersRepository;
    }

    @Transactional
    public void log(String action, String details) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new NotFoundException();
        }
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User user = usersRepository.findById(userDetails.getId()).orElseThrow(NotFoundException::new);

        ActivityLog log = new ActivityLog();
        log.setUser(user);
        log.setAction(action);
        log.setDetails(details + " by user with id " + user.getUserId());
        log.setTimestamp(LocalDateTime.now());

        activitiesLogsRepository.save(log);
    }

    @Transactional
    public void save(ActivityLog activityLog){
        activitiesLogsRepository.save(activityLog);
    }

    public List<ActivityLog> findAll(){
        return activitiesLogsRepository.findAll();
    }

    public ActivityLog findById(int id){
        return activitiesLogsRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public void edit(ActivityLog updatedActivityLog, int id) {
        ActivityLog activityLog = activitiesLogsRepository.findById(id).orElseThrow(NotFoundException::new);
        modelMapper.map(updatedActivityLog, activityLog);
        activitiesLogsRepository.save(activityLog);
    }

    @Transactional
    public void remove(int id){
        activitiesLogsRepository.deleteById(id);
    }
}
