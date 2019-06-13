package com.exadel.controller;

import com.exadel.service.InternService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class InternController {

    @Autowired
    InternService internService;

    @RequestMapping("/createIntern")
    public void createIntern(@RequestParam String firstName, @RequestParam String surname, @RequestParam String school, @RequestParam int hoursPerWeek, @RequestParam String[] internshipTime, ModelMap model){
        model.addAttribute("firstName", firstName);
        model.addAttribute("surname", surname);
        model.addAttribute("school", school);
        model.addAttribute("hoursPerWeek", hoursPerWeek);
        model.addAttribute("internshipTime", internshipTime);
        internService.createIntern(firstName, surname, school, hoursPerWeek, internshipTime);
    }

    @RequestMapping("/getAllIntern")
    public String intern(Model model) {
        model.addAttribute("interns", internService.getAllIntern());
        return "intern";
    }
}
