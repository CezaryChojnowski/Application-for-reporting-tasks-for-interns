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
    public ModelAndView createIntern(@RequestParam String firstName, @RequestParam String surname, @RequestParam String school, @RequestParam String email, @RequestParam int hoursPerWeek, @RequestParam(value="internshipTime[]") String[] internshipTime, ModelMap model){
        model.addAttribute("firstName", firstName);
        model.addAttribute("surname", surname);
        model.addAttribute("school", school);
        model.addAttribute("email", email);
        model.addAttribute("hoursPerWeek", hoursPerWeek);
        model.addAttribute("internshipTime[]", internshipTime);
        internService.createIntern(firstName, surname, school,email, hoursPerWeek, internshipTime);
        return new ModelAndView("redirect:/getAllIntern");
    }

    @RequestMapping("/getAllIntern")
    public String intern(Model model) {
        model.addAttribute("interns", internService.getAllIntern());
        return "intern";
    }

    @RequestMapping(value="/details/{email}")
    public String details(@PathVariable("email") String email, Model model)throws  NullPointerException{
        boolean check = true;
        Intern intern = internService.findTasksByEmail(email);
        try{
            List<Task> tasks = Arrays.asList(intern.getTasks());
            model.addAttribute("tasks", tasks);
        }catch (NullPointerException e){
            check = false;
            model.addAttribute("EmptyTasksList", check);
        }
        finally {
            model.addAttribute("EmptyTasksList", check);
            model.addAttribute("intern", intern);
            return "details";
        }
    }

    @RequestMapping("/delete/{email}")
    public ModelAndView delete(@PathVariable String email, Model model){
        Intern intern = internService.findInternByEmail(email);
        model.addAttribute("intern", intern);
        internService.delete(email);
        return new ModelAndView("redirect:/getAllIntern");
    }

    @RequestMapping("/editIntern/{id}")
    public String editIntern(@PathVariable ObjectId id, Model model){
        Intern intern = internService.findByid(id);
        model.addAttribute("intern", intern);
        return "internEditForm.html";
    }

    @RequestMapping("/updateIntern")
    public ModelAndView updateIntern(@RequestParam(required = false) ObjectId _id, @RequestParam String firstName, @RequestParam String surname, @RequestParam String school, @RequestParam String email, @RequestParam int hoursPerWeek, @RequestParam(value="internshipTime[]") String[] internshipTime, ModelMap model){
        model.addAttribute("firstName", firstName);
        model.addAttribute("surname", surname);
        model.addAttribute("school", school);
        model.addAttribute("email", email);
        model.addAttribute("hoursPerWeek", hoursPerWeek);
        model.addAttribute("internshipTime[]", internshipTime);
        internService.update(_id, firstName, surname, school, email, hoursPerWeek, internshipTime);
        return new ModelAndView("redirect:/getAllIntern");
    }


}
