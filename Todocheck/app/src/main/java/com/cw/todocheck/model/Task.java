package com.cw.todocheck.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "task_table")
public class Task {
    @ColumnInfo(name = "task_ID")
    @PrimaryKey(autoGenerate = true)
    public long taskID;

    public String task;

    public Priority priority;

    public String keywords;

    public String person;

    public String taskList;

    @ColumnInfo(name = "due_date")
    public Date dueDate;

    @ColumnInfo(name = "created_at")
    public Date dateCreated;

    @ColumnInfo(name = "is_done")
    public boolean isDone;


    public Task(String task, Priority priority, String keywords, String person, String taskList, Date dueDate, Date dateCreated, boolean isDone) {
        this.task = task;
        this.priority = priority;
        this.keywords = keywords;
        this.person = person;
        this.taskList = taskList;
        this.dueDate = dueDate;
        this.dateCreated = dateCreated;
        this.isDone = isDone;
    }

    public long getTaskID() {
        return taskID;
    }

    public void setTaskID(long taskID) {
        this.taskID = taskID;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String gettaskList() {
        return taskList;
    }

    public void settaskList(String taskList) {
        this.taskList = taskList;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskID=" + taskID +
                ", task='" + task + '\'' +
                ", priority=" + priority +
                ", keywords='" + keywords + '\'' +
                ", person='" + person + '\'' +
                ", taskList='" + taskList + '\'' +
                ", dueDate=" + dueDate +
                ", dateCreated=" + dateCreated +
                ", isDone=" + isDone +
                '}';
    }
}
