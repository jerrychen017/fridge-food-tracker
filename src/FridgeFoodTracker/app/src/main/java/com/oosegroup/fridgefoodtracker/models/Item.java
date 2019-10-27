package com.oosegroup.fridgefoodtracker.models;

import java.util.Date;

/** Item Class */
public class Item {

    private int id;
    // private Description description;
    private String description;
    private Date dateEntered;
    private Date dateExpired;

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

    public int getId() {
        return id;
    }

//  public Description getDescription() {
//    return description;
//  }

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

//  public void setDescription(Description description) {
//    this.description = description;
//  }

    public void setDateEntered(Date dateEntered) {
        this.dateEntered = dateEntered;
    }

    public void setDateExpired(Date dateExpired) {
        this.dateExpired = dateExpired;
    }
}