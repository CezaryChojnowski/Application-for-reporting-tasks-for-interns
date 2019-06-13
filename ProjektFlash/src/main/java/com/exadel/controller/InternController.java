package com.exadel.controller;

import com.exadel.model.Intern;
import com.exadel.model.Task;
import com.exadel.service.InternService;
import org.bson.types.ObjectId;
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
public class InternController {

    @Autowired
    InternService internService;

    @RequestMapping("newIntern")
    public String newIntern(Model model){
        model.addAttribute("intern", new Intern());
        return "internform.html";
    }

    @RequestMapping("/createIntern")
    public ModelAndView createIntern(@RequestParam String firstName, @RequestParam String surname, @RequestParam String school, @RequestParam int hoursPerWeek, @RequestParam(value="internshipTime[]") String[] internshipTime, ModelMap model){
        model.addAttribute("firstName", firstName);
        model.addAttribute("surname", surname);
        model.addAttribute("school", school);
        model.addAttribute("hoursPerWeek", hoursPerWeek);
        model.addAttribute("internshipTime[]", internshipTime);
//        model.addAttribute("internshipTime2", internshipTime[1]);
        internService.createIntern(firstName, surname, school, hoursPerWeek, internshipTime);
        return new ModelAndView("redirect:/getAllIntern");
    }

    @RequestMapping("/getAllIntern")
    public String intern(Model model) {
        model.addAttribute("interns", internService.getAllIntern());
        return "intern";
    }

    @RequestMapping(value="/details/{id}")
    public String details(@PathVariable("id") ObjectId id, Model model){
        Intern intern = internService.findByid(id);
        List<Task> tasks = Arrays.asList(intern.getTasks());
        System.out.println(tasks);
        model.addAttribute("tasks", tasks);
        model.addAttribute("intern", intern);
        return "details";
    }



}
