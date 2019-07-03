package com.exadel.model;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

public class Task {
    @Id
    private ObjectId _id;
    private Date date;
    private int hours;
    private String task;
    private String EK;

    public Task(Date date, int hours, String task, String EK) {
        ObjectId _id = new ObjectId();
        this._id=_id;
        this.date = date;
        this.hours = hours;
        this.task = task;
        this.EK = EK;
    }

    public Task() {
    }

    public ObjectId get_idTask() {
        return _id;
    }

    public void set_idTask(ObjectId _id) {
        this._id = _id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
