package com.exadel.controller;

import com.exadel.model.Intern;
import com.exadel.model.Task;
import com.exadel.service.InternService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
public class InternController {

    @Autowired
    LocalValidatorFactoryBean localValidatorFactoryBean;

    @Autowired
    ValidatingMongoEventListener ValidatingMongoEventListener;

    @Autowired
    InternService internService;

    @RequestMapping(value="/newIntern")
    public String newIntern(Model model){
        model.addAttribute("intern", new Intern());
        return "internform.html";
    }

    @RequestMapping(value="/createIntern")
    public ModelAndView createIntern(
            @Valid @RequestParam String firstName,
                                     @RequestParam String surname,
                                     @RequestParam String school,
                                     @RequestParam String email,
                                     @RequestParam int hoursPerWeek,
                                     @RequestParam(value="internshipTime[]")
                                     @DateTimeFormat(pattern = "yyyy-MM-dd") Date[] internshipTime,
                                     ModelMap model) throws ConstraintViolationException {
        try {
            boolean checkCorrectRange = internService.checkCorrectRange(internshipTime[0], internshipTime[1]);
            if (internService.checkIfTheEmailIsUnique(email) == false) {
                return new ModelAndView("redirect:/newIntern");
            }
            if (!checkCorrectRange) {
                return new ModelAndView("redirect:/newIntern");
            }
            internService.createIntern(firstName, surname, school, email, hoursPerWeek, internshipTime);
            //internService.createIntern(intern.getFirstName(), intern.getSurname(), intern.getSchool(),intern.getEmail(), intern.getHoursPerWeek(), intern.getInternshipTime());
            return new ModelAndView("redirect:/getAllIntern");
        }
        catch (ConstraintViolationException c){
            return new ModelAndView("redirect:/newIntern");
        }
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

    @RequestMapping("/preReport/{email}/{check}")
    public String report(@PathVariable("email") String email, @PathVariable boolean check, Model model){
        Intern intern = internService.findInternByEmail(email);
        model.addAttribute("intern", intern);
        model.addAttribute("check", check);
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
                return "redirect:/preReport/" + email +"/" + checkCorrectRange;
            }
            return "reportTaskBeetwenTwoDates";
        }catch (NullPointerException e){
            return "redirect:/preReport/" + email;
        }
    }
}
