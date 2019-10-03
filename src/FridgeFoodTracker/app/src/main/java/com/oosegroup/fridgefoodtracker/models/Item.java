package com.oosegroup.fridgefoodtracker.models;

// item class representing Item
public class Item {
  private int id;
  private Description description;
  private int timeEntered;
  private int timeExpired;

  public Item(int id) {
    this.id = id;
  }


  public int getId() {
    return id;
  }

  public Description getDescription() {
    return description;
  }

  public int getTimeEntered() {
    return timeEntered;
  }

  public int getTimeExpired() {
    return timeExpired;
  }

  public void setDescription(Description description) {
    this.description = description;
  }

  public void setTimeEntered(int timeEntered) {
    this.timeEntered = timeEntered;
  }

  public void setTimeExpired(int timeExpired) {
    this.timeExpired = timeExpired;
  }
}