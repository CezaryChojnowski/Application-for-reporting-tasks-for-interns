package com.exadel.controller;

import com.exadel.model.Intern;
import com.exadel.model.Task;
import com.exadel.service.InternService;
import com.exadel.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private InternService internService;

    @RequestMapping("/newTask")
    public String newTask(Model model){
        model.addAttribute("task", new Task());
        return "reportingTask.html";
    }

    @RequestMapping(value="/createTask/{email}")
    public ModelAndView createTask(@PathVariable("email") String email, @RequestParam String date, @RequestParam int hours, @RequestParam String task, @RequestParam String EK, ModelMap model){
        model.addAttribute("date", date);
        model.addAttribute("hours", hours);
        model.addAttribute("task", task);
        Intern intern = internService.findTasksByEmail(email);
        List<Task> tasks = Arrays.asList(intern.getTasks());
        Task tempTask = taskService.createTask(date, hours, task, EK);
        tasks.add(tempTask);
        Object[] array = tasks.toArray();
        intern.setTasks((Task[]) array);
        return new ModelAndView("redirect:/details" + intern.getEmail());
    }
}

