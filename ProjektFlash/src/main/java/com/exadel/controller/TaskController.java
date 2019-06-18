package com.exadel.controller;

import com.exadel.model.Intern;
import com.exadel.model.Task;
import com.exadel.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TaskController {

    @Autowired
    private TaskService taskService;

    @RequestMapping("/newTask")
    public String newIntern(Model model){
        model.addAttribute("task", new Task());
        return "reportingTask.html";
    }

    @RequestMapping("/createTask")
    public ModelAndView createIntern(@RequestParam String date, @RequestParam int hours, @RequestParam String task, @RequestParam String EK, ModelMap model){
        model.addAttribute("date", date);
        model.addAttribute("hours", hours);
        model.addAttribute("task", task);
        taskService.createTask(date, hours, task, EK);
        return new ModelAndView("redirect:/getAllIntern");
    }
}
