package com.exadel.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "intern")
public class Intern {
    private ObjectId _id;
    private String firstName;
    private String surname;
    private String school;
    private String email;
    private String role;
    private String[] internshipTime;
    private int hoursPerWeek;
    private int totalHoursPerWeek;
    private List<Task> tasks;

    public Intern() {
    }

    public Intern(String firstName, String surname, String school, String email, String[] internshipTime, int hoursPerWeek) {
        this.firstName = firstName;
        this.surname = surname;
        this.school = school;
        this.email = email;
        this.internshipTime = internshipTime;
        this.hoursPerWeek = hoursPerWeek;
    }


    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String[] getInternshipTime() {
        return internshipTime;
    }

    public void setInternshipTime(String[] internshipTime) {
        this.internshipTime = internshipTime;
    }

    public int getHoursPerWeek() {
        return hoursPerWeek;
    }

    public void setHoursPerWeek(int hoursPerWeek) {
        this.hoursPerWeek = hoursPerWeek;
    }

    public int getTotalHoursPerWeek() {
        return totalHoursPerWeek;
    }

    public void setTotalHoursPerWeek(int totalHoursPerWeek) {
        this.totalHoursPerWeek = totalHoursPerWeek;
    }

}
