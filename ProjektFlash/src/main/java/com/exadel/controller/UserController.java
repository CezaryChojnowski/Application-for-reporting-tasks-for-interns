package com.exadel.controller;

import com.exadel.model.User;
import com.exadel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/newUser")
    public String createUser(Model model){
        model.addAttribute("user", new User());
        return "userform.html";
    }

    @RequestMapping("/user")
    public String user(Model model){
        model.addAttribute("users", userService.getAllUsers());
        return "user.html";
    }

    @RequestMapping("/createUser")
    public String createUser(@RequestParam String login, @RequestParam String pass, @RequestParam String phone, @RequestParam String email){
        User u = userService.createUser(login,pass, phone, email);
        return "redirect:/user";
    }

    @RequestMapping("/getAllUsers")
    public String product(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "user";
    }
}
