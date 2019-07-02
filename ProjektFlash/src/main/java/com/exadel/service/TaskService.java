package com.exadel.service;

import com.exadel.model.Task;
import com.exadel.repository.TaskRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    public Task createTask(Date date, int hours, String task, String EK){
        return new Task(date,hours,task,EK);
    }

    public Task findByid(ObjectId _idTask){
        return taskRepository.findTaskByid(_idTask);
    }
}
