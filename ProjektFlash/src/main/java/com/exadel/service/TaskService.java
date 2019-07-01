package com.exadel.service;

import com.exadel.model.Task;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TaskService {

    public Task createTask(Date date, int hours, String task, String EK){
        return new Task(date,hours,task,EK);
    }
}
