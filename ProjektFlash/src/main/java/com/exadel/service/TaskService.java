package com.exadel.service;

import com.exadel.model.Task;
import com.exadel.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(Date date, int hours, String task, String EK){
        return new Task(date,hours,task,EK);
    }
}
