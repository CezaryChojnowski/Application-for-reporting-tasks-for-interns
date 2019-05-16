package com.exadel.service;

import com.exadel.model.Contact;
import com.exadel.model.User;
import com.exadel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getByLogin(String login){
        return userRepository.findByLogin(login);
    }

    public Contact createContact(String email, String phone){
        return new Contact(email, phone);
    }

    public User createUser(String login, String pass, String email, String phone){
        User user = new User();
        user.setLogin(login);
        user.setPass(pass);
        user.setActive(true);
        String[] role = {"user"};
        user.setRole(role);
        user.setContact(createContact(email,phone));
        return userRepository.save(user);
    }
}
