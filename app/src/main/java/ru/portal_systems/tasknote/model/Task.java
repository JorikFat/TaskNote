package ru.portal_systems.tasknote.model;

import java.util.Date;

/**
 * Created by 111 on 18.08.2017.
 */

public class Task {
    private long id;
    private String name;
    private String description;
    private Date term;
    private int prioritet;
    private boolean complete;

    public Task(){}

    public Task(String name, String description, Date term, int prioritet) {
        this.name = name;
        this.description = description;
        this.term = term;
        this.prioritet = prioritet;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTerm() {
        return term;
    }

    public void setTerm(Date term) {
        this.term = term;
    }

    public int getPrioritet() {
        return prioritet;
    }

    public void setPrioritet(int prioritet) {
        this.prioritet = prioritet;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
