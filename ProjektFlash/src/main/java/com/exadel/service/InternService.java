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

    public Intern findTasksByEmail(String email){
        return internRepository.findTasksByEmail(email);
    }

    public Intern findInternByEmail(String email){
        return internRepository.findInternByEmail(email);
    }

    public List<Task> findTasksByid(ObjectId id){
        return internRepository.findTasksByid(id);
    }

    public Intern createIntern(String firstName, String surname, String school, String email, int hoursPerWeek, String[] internshipTime){
        Intern intern = new Intern();
        intern.setFirstName(firstName);
        intern.setSurname(surname);
        intern.setSchool(school);
        intern.setEmail(email);
        intern.setRole("intern");
        intern.setInternshipTime(internshipTime);
        intern.setHoursPerWeek(hoursPerWeek);
        return internRepository.save(intern);
    }

    public void delete(String email){
        Intern intern = internRepository.findInternByEmail(email);
        internRepository.delete(intern);
    }

    public Intern update(ObjectId _id, String firstName, String surname, String school, String email, int hoursPerWeek, String[] internshipTime){
        Intern intern = internRepository.findByid(_id);
        intern.set_id(_id);
        intern.setFirstName(firstName);
        intern.setSurname(surname);
        intern.setSchool(school);
        intern.setEmail(email);
        intern.setRole("intern");
        intern.setInternshipTime(internshipTime);
        intern.setHoursPerWeek(hoursPerWeek);
        return internRepository.save(intern);
    }

    public Intern updateTasks(String email, Task task){
        Intern intern = findTasksByEmail(email);
        List<Task> tasks = intern.getTasks();
        tasks.add(task); //
        intern.setTasks(tasks); //
        return internRepository.save(intern);
    }


}
