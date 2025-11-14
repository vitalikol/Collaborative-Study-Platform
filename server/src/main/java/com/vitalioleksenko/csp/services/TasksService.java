package com.vitalioleksenko.csp.services;

import com.vitalioleksenko.csp.models.Task;
import com.vitalioleksenko.csp.repositories.TasksRepository;
import com.vitalioleksenko.csp.util.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TasksService {
    private final TasksRepository tasksRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public TasksService(TasksRepository tasksRepository, ModelMapper modelMapper) {
        this.tasksRepository = tasksRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void save(Task task){
        tasksRepository.save(task);
    }

    public List<Task> findAll(){
        return tasksRepository.findAll();
    }

    public Task findById(int id){
        return tasksRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public List<Task> findByUserId(int id){
        return tasksRepository.findByUser_UserId(id);
    }

    @Transactional
    public void edit(Task updatedTask, int id){
        Task task = tasksRepository.findById(id).orElseThrow(NotFoundException::new);
        modelMapper.map(updatedTask, task);
        tasksRepository.save(task);
    }

    @Transactional
    public void remove(int id){
        tasksRepository.deleteById(id);
    }
}
