package com.exadel.controller;

import com.exadel.model.Intern;
import com.exadel.model.Task;
import com.exadel.repository.InternRepository;
import com.exadel.service.InternService;
import com.exadel.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Controller
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private InternService internService;

    @RequestMapping("/newTask/{email}")
    public String newTask(@PathVariable("email") String email, Model model){
        Intern intern = internService.findInternByEmail(email);
        model.addAttribute("task", new Task());
        model.addAttribute("intern", intern);
        return "reportingTask.html";
    }

    @RequestMapping("/createTask")
    public ModelAndView createTask(@RequestParam(required = false) String email, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date, @RequestParam int hours, @RequestParam String task, @RequestParam String EK, ModelMap model){
        model.addAttribute("date", date);
        model.addAttribute("hours", hours);
        model.addAttribute("task", task);
        model.addAttribute("EK", EK);
        Task newTask = taskService.createTask(date, hours, task, EK); //
        System.out.println(email);
        internService.updateTasks(email, newTask);
        return new ModelAndView("redirect:/details/" + email);
    }
}


