package com.vitalioleksenko.csp.services;

import com.vitalioleksenko.csp.models.ActivityLog;
import com.vitalioleksenko.csp.repositories.ActivitiesLogsRepository;
import com.vitalioleksenko.csp.util.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ActivitiesLogsService {
    private final ActivitiesLogsRepository activitiesLogsTaskRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ActivitiesLogsService(ActivitiesLogsRepository activitiesLogsTaskRepository, ModelMapper modelMapper) {
        this.activitiesLogsTaskRepository = activitiesLogsTaskRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void save(ActivityLog activityLog){
        activitiesLogsTaskRepository.save(activityLog);
    }

    public List<ActivityLog> findAll(){
        return activitiesLogsTaskRepository.findAll();
    }

    public ActivityLog findById(int id){
        return activitiesLogsTaskRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public void edit(ActivityLog updatedActivityLog, int id){
        ActivityLog activityLog = activitiesLogsTaskRepository.findById(id).orElseThrow(NotFoundException::new);
        modelMapper.map(updatedActivityLog, activityLog);
        activitiesLogsTaskRepository.save(activityLog);
    }

    @Transactional
    public void remove(int id){
        activitiesLogsTaskRepository.deleteById(id);
    }
}
