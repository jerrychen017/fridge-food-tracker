package com.oosegroup.fridgefoodtracker.models;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

// item class representing Item
public class Item {
    private int id;
    // private Description description;
    private String description;
    private Date dateEntered;
    private Date dateExpired;

    /**
     * Item can be initialized with an id when having a local fridge
     * @param id item id
     */
    public Item(int id) {
        this.dateEntered = new Date();
        this.dateExpired = new Date(this.getDateEntered().getTime() + 86400000); // 86400000 msec in 1 day
        this.id = id;
    }

    public Item(int id, String description) {
        this.dateEntered = new Date();
        this.dateExpired = new Date(this.getDateEntered().getTime() + 86400000);
        this.id = id;
        this.description = description;
    }

    public Item(String description) {
        this.dateEntered = new Date();
        this.dateExpired = new Date(this.getDateEntered().getTime() + 86400000);
        this.description = description;
    }

    public int getId() {
        return id;
    }

    /**
     * When having a remote fridge, item id is assigned by the server
     * @param id id to be assigned
     */
    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public Date getDateEntered() {
        return dateEntered;
    }

    public Date getDateExpired() {
        return dateExpired;
    }

    public void setDateEntered(Date dateEntered) {
        this.dateEntered = dateEntered;
    }

    public void setDateExpired(Date dateExpired) {
        this.dateExpired = dateExpired;
    }
}