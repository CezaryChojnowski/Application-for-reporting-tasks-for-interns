package com.exadel.controller;

import com.exadel.model.User;
import com.exadel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/newUser")
    public String createUser(Model model, @RequestParam(required=false) String login, @RequestParam(required=false) String email){
        model.addAttribute("user", new User());
        model.addAttribute("loginError", checkIfTheLoginIsUnique(login));
        model.addAttribute("emailError", checkIfTheEmailIsUnique(email));
        return "userform.html";
    }

    @RequestMapping("/createUser")
    public ModelAndView createUser(@RequestParam String login, @RequestParam String pass, @RequestParam String phone, @RequestParam String email, ModelMap model){
        boolean loginAlreadyInUse = checkIfTheLoginIsUnique(login);
        boolean emailAlreadyInUse = checkIfTheEmailIsUnique(email);
        model.addAttribute("login", login);
        model.addAttribute("email", email);
        if(loginAlreadyInUse || emailAlreadyInUse){
            return new ModelAndView("redirect:/newUser", model);
        }
            userService.createUser(login, pass, phone, email);
            return new ModelAndView("redirect:/login");
    }

    @RequestMapping("/getAllUsers")
    public String product(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "user";
    }

    @RequestMapping("/getUserByLogin")
    public boolean checkIfTheLoginIsUnique(@RequestParam String login){
        if(userService.getByLogin(login)==null){
            return false;
        }
        return true;
    }

    @RequestMapping("getUserByEmail")
    public boolean checkIfTheEmailIsUnique(@RequestParam String email){
        if(userService.findByEmail(email).isEmpty()){
            return false;
        }
        return true;
    }

//    @RequestMapping(value = "/login", method = RequestMethod.GET)
//    public ModelAndView login() {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("login");
//        return modelAndView;
//    }

//    @RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
//    public ModelAndView welcome() {
//        ModelAndView modelAndView = new ModelAndView();
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = userService.getByLogin(authentication.getName());
//        modelAndView.addObject("login", "Welcome " + user.getLogin() + " | Phone: " + user.getContact().getPhone() + " | Email: " + user.getContact().getEmail());
//        modelAndView.setViewName("welcome");
//        return modelAndView;
//    }
//
//    @RequestMapping(value = {"/welcome2"}, method = RequestMethod.GET)
//    public ModelAndView welcome2() {
//        ModelAndView modelAndView = new ModelAndView();
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = userService.getByLogin(authentication.getName());
//        modelAndView.addObject("login", "Welcome2 " + user.getLogin() + " | Phone: " + user.getContact().getPhone() + " | Email: " + user.getContact().getEmail());
//        modelAndView.setViewName("welcome2");
//        return modelAndView;
//    }
}
