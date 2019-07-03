package com.exadel.controller;

import com.exadel.model.Intern;
import com.exadel.model.Task;
import com.exadel.service.InternService;
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
import java.util.List;

@Controller
public class InternController {

    @Autowired
    InternService internService;

    @RequestMapping("/newIntern")
    public String newIntern(Model model){
        model.addAttribute("intern", new Intern());
        return "internform.html";
    }

    @RequestMapping("/createIntern")
    public ModelAndView createIntern(@RequestParam String firstName,
                                     @RequestParam String surname,
                                     @RequestParam String school,
                                     @RequestParam String email,
                                     @RequestParam int hoursPerWeek,
                                     @RequestParam(value="internshipTime[]")
                                     @DateTimeFormat(pattern = "yyyy-MM-dd") Date[] internshipTime,
                                     ModelMap model){
        if(internService.checkIfTheEmailIsUnique(email)==false){
            return new ModelAndView("redirect:/newIntern");
        }
        internService.createIntern(firstName, surname, school,email, hoursPerWeek, internshipTime);
        return new ModelAndView("redirect:/getAllIntern");
    }

    @RequestMapping("/getAllIntern")
    public String intern(Model model) {
        model.addAttribute("interns", internService.getAllIntern());
        return "intern";
    }

    @RequestMapping(value="/details/{email}")
    public String details(@PathVariable("email") String email,
                          Model model)throws  NullPointerException{
        boolean check = true;
        Intern intern = internService.findTasksByEmail(email);
        int totalHourse = 0;
        List<Task> tasks = intern.getTasks();
        try{
            for (Task t: tasks) {
                totalHourse = totalHourse + t.getHours();
            }
            model.addAttribute("tasks", tasks);
            model.addAttribute("totalHourse", totalHourse);
        }
        finally {
            if(tasks.isEmpty()){
                check = false;
                model.addAttribute("EmptyTasksList", check);
            }
            model.addAttribute("EmptyTasksList", check);
            model.addAttribute("intern", intern);
            return "details";
        }
    }

    @RequestMapping("/delete/{email}")
    public ModelAndView delete(@PathVariable String email,
                               Model model){
        Intern intern = internService.findInternByEmail(email);
        model.addAttribute("intern", intern);
        internService.delete(email);
        return new ModelAndView("redirect:/getAllIntern");
    }

    @RequestMapping("/editIntern/{id}")
    public String editIntern(@PathVariable ObjectId id,
                             Model model){
        Intern intern = internService.findByid(id);
        model.addAttribute("intern", intern);
        return "internEditForm.html";
    }

    @RequestMapping("/updateIntern")
    public ModelAndView updateIntern(@RequestParam(required = false) ObjectId _id,
                                     @RequestParam String firstName,
                                     @RequestParam String surname,
                                     @RequestParam String school,
                                     @RequestParam String email,
                                     @RequestParam int hoursPerWeek,
                                     @RequestParam(value="internshipTime[]")
                                     @DateTimeFormat(pattern = "yyyy-MM-dd") Date[] internshipTime,
                                     ModelMap model){
        internService.update(_id, firstName, surname, school, email, hoursPerWeek, internshipTime);
        return new ModelAndView("redirect:/getAllIntern");
    }

    @RequestMapping("/preReport/{email}")
    public String report(@PathVariable("email") String email, Model model){
        Intern intern = internService.findInternByEmail(email);
        model.addAttribute("intern", intern);
        return "reportForm";
    }

    @RequestMapping("/report")
    public String preReport(@RequestParam("email") String email,
                          @RequestParam(value="startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                          @RequestParam(value="finishDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date finishDate,
                           Model model) throws  NullPointerException{
        boolean checkCorrectRange = internService.checkCorrectRange(startDate,finishDate);
        int totalHourseInTheRange = 0;
        try{
            List<Task> taskResult = internService.findTasksBeetwenTwoDates(email,startDate,finishDate);
            for (Task t: taskResult) {
                totalHourseInTheRange = totalHourseInTheRange + t.getHours();
            }
            model.addAttribute("totalHourseInTheRange", totalHourseInTheRange);
            model.addAttribute("taskResult", taskResult);
            if(!checkCorrectRange){
                return "redirect:/preReport/" + email;
            }
            return "reportTaskBeetwenTwoDates";
        }catch (NullPointerException e){
            return "redirect:/preReport/" + email;
        }
    }
}
