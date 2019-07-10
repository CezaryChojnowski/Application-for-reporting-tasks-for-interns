package com.exadel.controller;

import com.exadel.model.Intern;
import com.exadel.model.Task;
import com.exadel.service.InternService;
import com.exadel.service.TaskService;
import org.bson.types.ObjectId;
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
    public String newTask(@PathVariable("email") String email,
                          Model model,
                          @RequestParam(defaultValue = "true") boolean checkIfTheTaskIsInTheRange,
                          @RequestParam(defaultValue = "true") boolean checkIfTheTimeIsInTheLimit,
                          @RequestParam(defaultValue = "true") boolean enterDate,
                          @RequestParam(defaultValue = "true") boolean emptyTask,
                          @RequestParam(defaultValue = "true") boolean emptyEK){
        Intern intern = internService.findInternByEmail(email);
        model.addAttribute("task", new Task());
        model.addAttribute("enterDate", enterDate);
        model.addAttribute("checkIfTheTaskIsInTheRange", checkIfTheTaskIsInTheRange);
        model.addAttribute("checkIfTheTimeIsInTheLimit", checkIfTheTimeIsInTheLimit);
        model.addAttribute("emptyTask", emptyTask);
        model.addAttribute("emptyEK", emptyEK);
        model.addAttribute("intern", intern);
        return "reportingTask.html";
    }

    @RequestMapping("/createTask")
    public ModelAndView createTask(@RequestParam(required = false) String email,
                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                   @RequestParam(defaultValue = "0") int hours,
                                   @RequestParam String task,
                                   @RequestParam String EK,
                                   ModelMap model) throws NullPointerException{
        try{
            Intern intern = internService.findInternByEmail(email);
            boolean checkIfTheTaskIsInTheRange = internService.checkIfTheTaskIsInTheRange(date, intern.getInternshipTime());
            boolean checkIfTheTimeIsInTheLimit = internService.checkIfTheTimeIsInTheLimit(hours);
            boolean emptyTask = !task.isEmpty();
            boolean emptyEK = !EK.isEmpty();
            if((checkIfTheTaskIsInTheRange==false) || (checkIfTheTimeIsInTheLimit==false) || emptyTask==false || emptyEK==false){
                model.addAttribute("emptyTask", emptyTask);
                model.addAttribute("emptyEK", emptyEK);
                model.addAttribute("checkIfTheTaskIsInTheRange", checkIfTheTaskIsInTheRange);
                model.addAttribute("checkIfTheTimeIsInTheLimit", checkIfTheTimeIsInTheLimit);
                return new ModelAndView("redirect:/newTask/" + email, model);
            }
            Task newTask = taskService.createTask(date, hours, task, EK);
            internService.addTask(email, newTask);
            return new ModelAndView("redirect:/details/" + email);
        }catch (NullPointerException n){
            boolean enterDate = false;
            model.addAttribute("enterDate", enterDate);
            return new ModelAndView("redirect:/newTask/" + email, model);
        }
    }

    @RequestMapping("/deleteTask/{email}/{_idTask}")
    public ModelAndView deleteTask(@PathVariable("email") String email,
                                   @PathVariable("_idTask") ObjectId _idTask, Model model){
        Intern intern = internService.findInternByEmail(email);
        model.addAttribute("intern", intern);
        internService.deleteTask(email, _idTask);
        return new ModelAndView("redirect:/details/" + email);
    }

    @RequestMapping("/editTask/{email}/{_idTask}")
    public String editIntern(@PathVariable String email,
                             @PathVariable ObjectId _idTask,
                             Model model,
                             @RequestParam(defaultValue = "true") boolean checkIfTheTaskIsInTheRange,
                             @RequestParam(defaultValue = "true") boolean checkIfTheTimeIsInTheLimit,
                             @RequestParam(defaultValue = "true") boolean enterDate,
                             @RequestParam(defaultValue = "true") boolean emptyTask,
                             @RequestParam(defaultValue = "true") boolean emptyEK){
        Intern intern = internService.findInternByEmail(email);
        Task task = internService.findTask(email, _idTask);
        model.addAttribute("checkIfTheTaskIsInTheRange", checkIfTheTaskIsInTheRange);
        model.addAttribute("checkIfTheTimeIsInTheLimit", checkIfTheTimeIsInTheLimit);
        model.addAttribute("emptyTask", emptyTask);
        model.addAttribute("emptyEK", emptyEK);
        model.addAttribute("enterDate", enterDate);
        model.addAttribute("task", task);
        model.addAttribute("intern", intern);
        return "taskEdit.html";
    }

    @RequestMapping("/updateTask")
    public ModelAndView updateIntern(@RequestParam(required = false) String email,
                                     @RequestParam(required = false) ObjectId _idTask,
                                     @RequestParam(value="date")
                                     @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                     @RequestParam int hours, @RequestParam String task,
                                     @RequestParam String EK, ModelMap model){
        try{
            Intern intern = internService.findInternByEmail(email);
            boolean checkIfTheTaskIsInTheRange = internService.checkIfTheTaskIsInTheRange(date, intern.getInternshipTime());
            boolean checkIfTheTimeIsInTheLimit = internService.checkIfTheTimeIsInTheLimit(hours);
            boolean emptyTask = !task.isEmpty();
            boolean emptyEK = !EK.isEmpty();
            if((checkIfTheTaskIsInTheRange==false) || (checkIfTheTimeIsInTheLimit==false) || emptyTask==false || emptyEK==false){
                model.addAttribute("emptyTask", emptyTask);
                model.addAttribute("emptyEK", emptyEK);
                model.addAttribute("checkIfTheTaskIsInTheRange", checkIfTheTaskIsInTheRange);
                model.addAttribute("checkIfTheTimeIsInTheLimit", checkIfTheTimeIsInTheLimit);
                return new ModelAndView("redirect:/editTask/" + email + "/" + _idTask, model);
            }
            internService.updateTask(email, _idTask, date, hours, task, EK);
            return new ModelAndView("redirect:/details/" + email);
        }catch (NullPointerException n){
            boolean enterDate = false;
            model.addAttribute("enterDate", enterDate);
            return new ModelAndView("redirect:/editTask/" + email + "/" + _idTask, model);
        }
    }
}


