package com.exadel.controller;

import com.exadel.model.Intern;
import com.exadel.model.Task;
import com.exadel.service.InternService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class InternController {

    @Autowired
    LocalValidatorFactoryBean localValidatorFactoryBean;

    @Autowired
    ValidatingMongoEventListener ValidatingMongoEventListener;

    @Autowired
    InternService internService;

    @RequestMapping(value= "/")
    public ModelAndView home(){
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws NullPointerException, ClassCastException{
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        try {
            Cookie[] cookies;
            Cookie tempCookie = new Cookie("temp", "temp");
            response.addCookie(tempCookie);
            cookies = request.getCookies();

            SecurityContext securityContext = SecurityContextHolder.getContext();
            Authentication authentication = securityContext.getAuthentication();
            String userRole = null, userEmail = null;
            if(!authentication.getPrincipal().equals("anonymousUser")) {
                try{
                    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                    userRole = String.valueOf(userDetails.getAuthorities());
                    userEmail = String.valueOf(userDetails.getUsername());

                    for (Cookie cookie : cookies) {
                        if (cookie.getName().equalsIgnoreCase("remember-me")) {
                            if(userRole.equals("[intern]")){
                                return new ModelAndView("redirect:/details/" + userEmail);
                            }
                            if(userRole.equals("[admin]")){
                                return new ModelAndView("redirect:/getAllIntern");
                            }
                        }
                    }
                }catch (ClassCastException c){
                    return modelAndView;
                }
            }

        }catch (NullPointerException e){
            return modelAndView;
        }
        return modelAndView;
    }

    @RequestMapping(value = "/accessDenied", method = RequestMethod.GET)
    public ModelAndView accesssDenied() {
        ModelAndView model = new ModelAndView();
        model.setViewName("accessDenied");
        return model;
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public ModelAndView handleError() {
        ModelAndView model = new ModelAndView();
        model.setViewName("error");
        return model;
    }


    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(value="/newIntern")
    public String newIntern(Model model,
                            @RequestParam(value="messages",
                                    required=false,
                                    defaultValue = "!empty") Set<String> messages,
                            @RequestParam(required=false,
                                    defaultValue = "true") boolean checkIfTheEmailIsUnique,
                            @RequestParam(required=false,
                                    defaultValue = "true") boolean checkCorrectRange) throws NullPointerException{
        try {
            model.addAttribute("messages", messages);
            model.addAttribute("checkIfTheEmailIsUnique", checkIfTheEmailIsUnique);
            model.addAttribute("checkCorrectRange", checkCorrectRange);
            model.addAttribute("intern", new Intern());
            return "internform.html";
        }
        catch(NullPointerException n){
            Set<String> myEmptySet = Collections.<String>emptySet();
            messages = myEmptySet;
            model.addAttribute("messages", messages);
            model.addAttribute("checkIfTheEmailIsUnique", checkIfTheEmailIsUnique);
            model.addAttribute("checkCorrectRange", checkCorrectRange);
            model.addAttribute("intern", new Intern());
            return "internform.html";
        }
    }
    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(value="/createIntern")
    public ModelAndView createIntern(
                                     @RequestParam String firstName,
                                     @RequestParam String surname,
                                     @RequestParam String school,
                                     @RequestParam String acronym,
                                     @RequestParam String email,
                                     @RequestParam String pass,
                                     @RequestParam(defaultValue = "0") int hoursPerWeek,
                                     @RequestParam(value="internshipTime[]")
                                     @DateTimeFormat(pattern = "yyyy-MM-dd") Date[] internshipTime,
                                     ModelMap model) throws ConstraintViolationException,
            NullPointerException, MethodArgumentTypeMismatchException, NumberFormatException {
        try {
            boolean checkCorrectRange = internService.checkCorrectRange(internshipTime[0], internshipTime[1]);
            boolean checkIfTheEmailIsUnique = internService.checkIfTheEmailIsUnique(email);
            if (!checkIfTheEmailIsUnique) {
                model.addAttribute("checkIfTheEmailIsUnique", checkIfTheEmailIsUnique);
                return new ModelAndView("redirect:/newIntern", model);
            }
            if (!checkCorrectRange) {
                model.addAttribute("checkCorrectRange",checkCorrectRange);
                return new ModelAndView("redirect:/newIntern", model);
            }
            internService.createIntern(firstName, surname, school, acronym, email, pass, hoursPerWeek, internshipTime);
            return new ModelAndView("redirect:/getAllIntern");
        }
        catch (ConstraintViolationException c){
            Set<ConstraintViolation<?>> constraintViolations = c.getConstraintViolations();
            Set<String> messages = new HashSet<>(constraintViolations.size());
            messages.addAll(constraintViolations.stream()
                    .map(constraintViolation -> String.format("%s", constraintViolation.getPropertyPath(),
                            constraintViolation.getInvalidValue(), constraintViolation.getMessage()))
                    .collect(Collectors.toList()));
            Iterator<String> it = messages.iterator();
            model.addAttribute("messages", messages);
            return new ModelAndView("redirect:/newIntern", model);
        }
        catch (NullPointerException n){
            return new ModelAndView("redirect:/newIntern");
        }
    }
    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping("/getAllIntern")
    public String intern(Model model) {
        model.addAttribute("interns", internService.getAllIntern());
        return "intern";
    }

    @PreAuthorize("#email == authentication.principal.username or hasAuthority('admin')")
    @RequestMapping(value="/details/{email}" , method = RequestMethod.GET)
    public String details(@PathVariable("email") String email,
                          @RequestParam(required=false,
                                defaultValue = "true") boolean somethingItsWrong,
                          Model model) throws NullPointerException, InterruptedException {
        model.addAttribute("somethingItsWrong", somethingItsWrong);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Authentication authentication = securityContext.getAuthentication();
            String userRole = null;
            if (authentication != null) {

                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                userRole = String.valueOf(userDetails.getAuthorities());
            }
        model.addAttribute("role", userRole);
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
    @PreAuthorize("hasAuthority('admin')")
    public ModelAndView delete(@PathVariable String email,
                               Model model){
        Intern intern = internService.findInternByEmail(email);
        model.addAttribute("intern", intern);
        internService.delete(email);
        return new ModelAndView("redirect:/getAllIntern");
    }

//    @PreAuthorize("#id == authentication.getPrincipal().get_id() or hasAuthority('admin')")
    @RequestMapping("/editIntern/{id}")
    public String editIntern(@PathVariable ObjectId id,
                             Model model,
                             @RequestParam(value="messages",
                                     required=false,
                                     defaultValue = "!empty") Set<String> messages,
                             @RequestParam(required=false,
                                     defaultValue = "true") boolean checkCorrectRange) throws NullPointerException{
        Intern intern = internService.findByid(id);
        try {
            model.addAttribute("messages", messages);
            model.addAttribute("checkCorrectRange", checkCorrectRange);
            model.addAttribute("intern", intern);
            return "internEditForm.html";
        }
        catch(NullPointerException n){
            Set<String> myEmptySet = Collections.<String>emptySet();
            messages = myEmptySet;
            model.addAttribute("messages", messages);
            model.addAttribute("checkCorrectRange", checkCorrectRange);
            model.addAttribute("intern", intern);
            return "internEditForm.html";
        }
    }

    @PreAuthorize("#email == authentication.principal.username")
    @RequestMapping(value = "/changePassword/{email}")
    public String changePassword(@PathVariable String email,
                                 @RequestParam(required=false,
                                         defaultValue = "true") boolean theSame,
                                 @RequestParam(required=false,
                                         defaultValue = "true") boolean tempCurrentPassword,
                                 Model model){
        Intern intern = internService.findInternByEmail(email);
        model.addAttribute("theSame", theSame);
        model.addAttribute("tempCurrentPassword", tempCurrentPassword);
        model.addAttribute("intern", intern);
        return "changePassword.html";
    }

    @PreAuthorize("#email == authentication.principal.username")
    @RequestMapping("/changePass")
    public ModelAndView changePass(@RequestParam String email,
                                   @RequestParam String currentPassword,
                                   @RequestParam String newPassword,
                                   @RequestParam String newPassword2, ModelMap model){
        boolean theSame = true;
        boolean tempCurrentPassword = true;
        boolean somethingItsWrong = false;
        Intern intern = internService.findInternByEmail(email);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(encoder.matches(currentPassword, intern.getPassword())){
            if(newPassword.equals(newPassword2)){
                intern.setPassword(encoder.encode(newPassword));
            }else{
                theSame = false;
                model.addAttribute("theSame", theSame);
                return new ModelAndView("redirect:/changePassword/" + intern.getEmail(), model);
            }
        }else{
            tempCurrentPassword = false;
            model.addAttribute("tempCurrentPassword", tempCurrentPassword);
            return new ModelAndView("redirect:/changePassword/" + intern.getEmail(), model);
        }
        internService.changePassword(email,currentPassword,newPassword, newPassword2);
        model.addAttribute("somethingItsWrong", somethingItsWrong);
        return new ModelAndView("redirect:/details/" + email, model);
    }

    @PreAuthorize("#email == authentication.principal.username or hasAuthority('admin')")
    @RequestMapping("/updateIntern")
    public ModelAndView updateIntern(@RequestParam(required = false) ObjectId _id,
                                     @RequestParam String firstName,
                                     @RequestParam String surname,
                                     @RequestParam String school,
                                     @RequestParam String acronym,
                                     @RequestParam String email,
                                     @RequestParam(defaultValue = "0") int hoursPerWeek,
                                     @RequestParam(value="internshipTime[]")
                                     @DateTimeFormat(pattern = "yyyy-MM-dd") Date[] internshipTime,
                                     ModelMap model){
        try {
            boolean checkCorrectRange = internService.checkCorrectRange(internshipTime[0], internshipTime[1]);
            if (!checkCorrectRange) {
                model.addAttribute("checkCorrectRange",checkCorrectRange);
                return new ModelAndView("redirect:/editIntern/" + _id, model);
            }
            internService.update(_id, firstName, surname, school, acronym, email, hoursPerWeek, internshipTime);
            return new ModelAndView("redirect:/details/" + email);
        }
        catch (ConstraintViolationException c){
            Set<ConstraintViolation<?>> constraintViolations = c.getConstraintViolations();
            Set<String> messages = new HashSet<>(constraintViolations.size());
            messages.addAll(constraintViolations.stream()
                    .map(constraintViolation -> String.format("%s", constraintViolation.getPropertyPath(),
                            constraintViolation.getInvalidValue(), constraintViolation.getMessage()))
                    .collect(Collectors.toList()));
            model.addAttribute("messages", messages);
            return new ModelAndView("redirect:/editIntern/" + _id, model);
        }
        catch (NullPointerException n){
            return new ModelAndView("redirect:/newIntern");
        }
    }

    @PreAuthorize("#email == authentication.principal.username or hasAuthority('admin')")
    @RequestMapping("/preReport/{email}/{check}")
    public String report(@PathVariable("email") String email, @PathVariable boolean check, Model model){
        Intern intern = internService.findInternByEmail(email);
        model.addAttribute("intern", intern);
        model.addAttribute("check", check);
        return "reportForm";
    }

    @PreAuthorize("#email == authentication.principal.username or hasAuthority('admin')")
    @RequestMapping("/report")
    public String preReport(@RequestParam("email") String email,
                            @RequestParam(value="startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                            @RequestParam(value="finishDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date finishDate,
                            Model model) throws NullPointerException{
        try{
            boolean checkCorrectRange = internService.checkCorrectRange(startDate,finishDate);
            int totalHourseInTheRange = 0;
            Intern intern = internService.findInternByEmail(email);
            List<Task> taskResult = internService.findTasksBeetwenTwoDates(email,startDate,finishDate);
            model.addAttribute("startDate", startDate);
            model.addAttribute("finishDate", finishDate);
            for (Task t: taskResult) {
                totalHourseInTheRange = totalHourseInTheRange + t.getHours();
            }
            model.addAttribute("intern", intern);
            model.addAttribute("totalHourseInTheRange", totalHourseInTheRange);
            model.addAttribute("taskResult", taskResult);
            if(!checkCorrectRange){
                return "redirect:/preReport/" + email +"/" + checkCorrectRange;
            }
            return "reportTaskBeetwenTwoDates";
        }catch (NullPointerException n){
            return "redirect:/preReport/" + email + "/" + true;
        }
    }
}
