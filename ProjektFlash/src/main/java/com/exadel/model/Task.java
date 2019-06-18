package com.exadel.model;


public class Task {
    private String date;
    private int hours;
    private String task;
    private String EK;

    public Task(String date, int hours, String task, String EK) {
        this.date = date;
        this.hours = hours;
        this.task = task;
        this.EK = EK;
    }

    public Task() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getEK() {
        return EK;
    }

    public void setEK(String EK) {
        this.EK = EK;
    }

}
