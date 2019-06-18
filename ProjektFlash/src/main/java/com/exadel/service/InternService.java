package com.exadel.service;

import com.exadel.model.Intern;
import com.exadel.model.Task;
import com.exadel.model.User;
import com.exadel.repository.InternRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InternService {

    @Autowired
    InternRepository internRepository;

    public List<Intern> getAllIntern(){
        return internRepository.findAll();
    }

    public Intern findByid(ObjectId id){
        return internRepository.findByid(id);
    }

    public List<Task> findTasksByid(ObjectId id){
        return internRepository.findTasksByid(id);
    }

    public Intern createIntern(String firstName, String surname, String school, int hoursPerWeek, String[] internshipTime){
        Intern intern = new Intern();
        intern.setFirstName(firstName);
        intern.setSurname(surname);
        intern.setSchool(school);
        intern.setInternshipTime(internshipTime);
        intern.setHoursPerWeek(hoursPerWeek);
        return internRepository.save(intern);
    }
}
