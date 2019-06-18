package com.exadel.service;

import com.exadel.model.Task;
import com.exadel.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(String date, int hours, String task, String EK){
        return new Task(date,hours,task,EK);
    }
}
