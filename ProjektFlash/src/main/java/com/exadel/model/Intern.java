package com.exadel.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "intern")
public class Intern {
    private String id;
    private String firstName;
    private String surname;
    private String school;
    private String[] internshipTime;
    private int hoursPerWeek;
    private int totalHoursPerWeek;
    private Task[] tasks;


    public Intern() {
    }

    public Intern(String firstName, String surname, String school, String[] internshipTime, int hoursPerWeek) {
        this.firstName = firstName;
        this.surname = surname;
        this.school = school;
        this.internshipTime = internshipTime;
        this.hoursPerWeek = hoursPerWeek;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Task[] getTasks() {
        return tasks;
    }

    public void setTasks(Task[] tasks) {
        this.tasks = tasks;
    }
}
