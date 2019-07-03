package com.exadel.service;

import com.exadel.model.Intern;
import com.exadel.model.Task;
import com.exadel.repository.InternRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public Intern createIntern(String firstName, String surname, String school, String email, int hoursPerWeek, Date[] internshipTime){
        Intern intern = new Intern();
        intern.setFirstName(firstName);
        intern.setSurname(surname);
        intern.setSchool(school);
        intern.setEmail(email);
        intern.setRole("intern");
        intern.setInternshipTime(internshipTime);
        intern.setHoursPerWeek(hoursPerWeek);
        List<Task> emptyTasksList = new ArrayList<Task>();
        intern.setTasks(emptyTasksList);
        return internRepository.save(intern);
    }

    public void delete(String email){
        Intern intern = internRepository.findInternByEmail(email);
        internRepository.delete(intern);
    }

    public Intern update(ObjectId _id, String firstName, String surname, String school, String email, int hoursPerWeek, Date[] internshipTime){
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

    public Intern addTask(String email, Task task){
        Intern intern = findTasksByEmail(email);
        List<Task> tasks = intern.getTasks();
        tasks.add(task);
        intern.setTasks(tasks);
        return internRepository.save(intern);
    }

    public Intern deleteTask(String email, ObjectId _idTask){
        Intern intern = findTasksByEmail(email);
        List<Task> tasks = intern.getTasks();
        for (Task t: tasks) {
            if(t.get_idTask().equals(_idTask)){
                tasks.remove(t);
                intern.setTasks(tasks);
                break;
            }
        }
        return internRepository.save(intern);
    }

    public Intern updateTask(String email, ObjectId _idTask, Date date, int hours, String task, String EK){
        Intern intern = findTasksByEmail(email);
        List<Task> tasks = intern.getTasks();
        for (Task t: tasks) {
            if(t.get_idTask().equals(_idTask)){
                t.setDate(date);
                System.out.println(t.getTask());
                t.setHours(hours);
                t.setTask(task);
                t.setEK(EK);
                intern.setTasks(tasks);
                break;
            }
        }
        return internRepository.save(intern);
    }

    public Task findTask(String email, ObjectId _idTask){
        Intern intern = findTasksByEmail(email);
        List<Task> tasks = intern.getTasks();
        for (Task t: tasks) {
            if(t.get_idTask().equals(_idTask)){
                return t;
            }
        }
        return null;
    }

    public List<Task> findTasksBeetwenTwoDates(String email, Date startdate, Date finishdate){
        Intern intern = findTasksByEmail(email);
        List<Task> tasks = intern.getTasks();
        List<Task> taskRestult = new ArrayList<Task>();
        Date correctStartDate = subtractDays(startdate, 1);
        Date correctFinishDate = addDays(finishdate, 1);
        for (Task t: tasks) {
            if(t.getDate().after(correctStartDate ) && t.getDate().before(correctFinishDate)){
                taskRestult.add(t);
            }
        }
        return taskRestult;
    }
    public boolean checkCorrectRange(Date startDate, Date finishDate){
        if(startDate.after(finishDate)){
            return false;
        }
        return true;
    }

    public static Date addDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);

        return cal.getTime();
    }

    public static Date subtractDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, -days);

        return cal.getTime();
    }

    public boolean checkIfTheEmailIsUnique(String email){
        if(internRepository.findInternByEmail(email)==null){
            return true;
        }
        return false;
    }
}
