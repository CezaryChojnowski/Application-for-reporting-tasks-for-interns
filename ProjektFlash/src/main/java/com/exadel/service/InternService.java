package com.exadel.service;

import com.exadel.model.Intern;
import com.exadel.model.Task;
import com.exadel.repository.InternRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@Service
public class InternService {

    @Autowired
    InternRepository internRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncode;

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

    public Intern findInternByToken(String resetToken){
        return internRepository.findInternByToken(resetToken);
    }

    public List<Task> findTasksByid(ObjectId id){
        return internRepository.findTasksByid(id);
    }

    public Intern createIntern(String firstName, String surname, String school, String acronym, String email, String pass, int hoursPerWeek, Date[] internshipTime){
        Intern intern = new Intern();
        intern.setFirstName(firstName);
        intern.setSurname(surname);
        intern.setSchool(school);
        intern.setEmail(email);
        intern.setAcronym(acronym);
        intern.setRole("intern");
        intern.setInternshipTime(internshipTime);
        intern.setHoursPerWeek(hoursPerWeek);
        intern.setResetToken(String.valueOf(UUID.randomUUID()));
        List<Task> emptyTasksList = new ArrayList<Task>();
        intern.setTasks(emptyTasksList);
        intern.setPassword(bCryptPasswordEncode.encode(pass));
        return internRepository.save(intern);
    }

    public void delete(String email){
        Intern intern = internRepository.findInternByEmail(email);
        internRepository.delete(intern);
    }

    public Intern update(ObjectId _id, String firstName, String surname, String school, String acronym, String email, int hoursPerWeek, Date[] internshipTime){
        Intern intern = internRepository.findByid(_id);
        intern.set_id(_id);
        intern.setFirstName(firstName);
        intern.setSurname(surname);
        intern.setSchool(school);
        intern.setEmail(email);
        intern.setAcronym(acronym);
        intern.setRole("intern");
        intern.setInternshipTime(internshipTime);
        intern.setHoursPerWeek(hoursPerWeek);
        return internRepository.save(intern);
    }

    public Intern updateResetPassword(String resetToken, String password){
        Intern intern = internRepository.findInternByToken(resetToken);
        intern.setPassword(bCryptPasswordEncode.encode(password));
        intern.setResetToken(String.valueOf(UUID.randomUUID()));
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

    public Intern changePassword(String email, String currentPassword, String newPassword, String newPassword2){
        Intern intern = findInternByEmail(email);
        intern.setPassword(bCryptPasswordEncode.encode(newPassword));
        return internRepository.save(intern);
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

    public boolean checkIfTheTaskIsInTheRange(Date dateTask, Date[] internshipTime){
        internshipTime[0] = subtractDays(internshipTime[0], 1);
        internshipTime[1] = addDays(internshipTime[1], 1);
        if(dateTask.after(internshipTime[0]) && dateTask.before(internshipTime[1])){
           return true;
        }
        return false;
    }

    public boolean checkIfTheTimeIsInTheLimit(int workTime){
        if(workTime <=8 && workTime >=1){
            return true;
        }
        return false;
    }

    public boolean checkIfTheEmailIsUnique(String email){
        if(internRepository.findInternByEmail(email)==null){
            return true;
        }
        return false;
    }

    public boolean checkLengthPasswordCount(String password){
        if(password.length()>=8){
            return true;
        }
        return false;
    }

    public boolean checkUpperCaseCount(String password){
        int temp = 0;
        char[] charArray = password.toCharArray();
        for(int i=0; i< charArray.length; i++){
            if(Character.isLetter(charArray[i])){
                if(Character.isUpperCase(charArray[i])){
                    temp++;
                    return false;
                }
            }
        }
        if(temp>=1){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean checkLowerCaseCount(String password){
        int temp = 0;
        char[] charArray = password.toCharArray();
        for(int i=0; i< charArray.length; i++){
            if(Character.isLetter(charArray[i])){
                if(Character.isLowerCase(charArray[i])){
                    temp++;
                }
            }
        }
        if(temp>=1){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean checkDigitCount(String password){
        int temp = 0;
        char[] charArray = password.toCharArray();
        for(int i=0; i< charArray.length; i++){
            if(Character.isDigit(charArray[i])){
                temp++;
            }
        }
        if(temp>=1){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean checkSpecialCharacterCount(String password) {
        int temp = 0;
        String specialChars = "/*!@#$%^&*()\"{}_[]|\\?/<>,.";
        for (int i = 0; i < password.length(); i++) {
            if (specialChars.contains(password.substring(i, 1))) {
                temp++;
            }
        }
        if(temp>=1){
            return true;
        }
        else{
            return false;
        }
    }
}
