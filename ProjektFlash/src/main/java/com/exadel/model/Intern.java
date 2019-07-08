package com.exadel.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Document(collection = "intern")
@Validated
public class Intern {
    @Id
    private ObjectId _id;
    @NotEmpty(message = "FirstName can't be empty")
    private String firstName;
    @NotEmpty(message = "Surname can't be empty")
    private String surname;
    @NotEmpty(message = "School can't be empty")
    private String school;
    @NotEmpty(message = "Email can't be empty")
    @Email(message = "Email can't be in this format")
    private String email;
    private String role;
    @NotEmpty(message = "Intership time can't be empty")
    @NotNull(message = "Intership time can't be null")
    private Date[] internshipTime;
    @NotNull(message = "Hours per week can't be null")
    @Min(value = 1, message="Hours per week can't be lesthean 1")
    @Max(value = 40, message ="Hours per week can't be greater than 40")
    private int hoursPerWeek;
    private int totalHoursPerWeek;
    private List<Task> tasks;

    public Intern() {
    }

    public Intern(@NotEmpty(message = "FirstName can't be empty") String firstName, @NotEmpty(message = "Surname can't be empty") String surname, @NotEmpty(message = "School can't be empty") String school, @Email(message = "Email can not be in this format") String email, @NotNull(message = "Hours per week can't be null") Date[] internshipTime, @NotNull(message = "Hours per week can't be null") @Min(value = 1, message = "Hours per week can't be lesthean 1") @Max(value = 40, message = "Hours per week can't be greater than 40") int hoursPerWeek) {
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

    public Date[] getInternshipTime() {
        return internshipTime;
    }

    public void setInternshipTime(Date[] internshipTime) {
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
