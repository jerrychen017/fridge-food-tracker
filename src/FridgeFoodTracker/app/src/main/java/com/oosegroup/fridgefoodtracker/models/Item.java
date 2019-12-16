package com.oosegroup.fridgefoodtracker.models;
import java.util.Date;

/**
 * Represents a fridge item with id, text description, entry date and expired date
 */
public class Item {
    private int id; // item id, which is uniquely assigned by the server
    private String description; // text description of an item
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

    /**
     * Construct an item with given id and text description for testing purpose
     * @param id
     * @param description
     */
    public Item(int id, String description) {
        this.dateEntered = new Date();
        this.dateExpired = new Date(this.getDateEntered().getTime() + 86400000);
        this.id = id;
        this.description = description;
    }

    /**
     * Construct and item with a description, initialize the entry date to be today,
     * and expiration date to be tomorrow
     * @param description item description
     */
    public Item(String description) {
        this.dateEntered = new Date();
        this.dateExpired = new Date(this.getDateEntered().getTime() + 86400000);
        this.description = description;
    }

    /**
     * Construct an item with a description and an expiration date
     * @param description item description
     * @param exp item expiration date
     */
    public Item(String description, Date exp) {
        this.dateEntered = new Date();
        this.dateExpired = exp;
        this.description = description;
    }

    /**
     * Construct an item with id, text description, and a lifespan of this item
     * @param id item id
     * @param description item description
     * @param lifespan how long can this item last
     */
    public Item(int id, String description, long lifespan) {
        this.dateEntered = new Date();
        this.dateExpired = new Date(this.getDateEntered().getTime() + lifespan);
        this.id = id;
        this.description = description;
    }

    /**
     * gets the id of this item
     * @return item id
     */
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

    /**
     * gets the text description of this item
     * @return item description
     */
    public String getDescription() {
        return description;
    }

    /**
     * sets teh text description of this item
     * @param desc text description
     */
    public void setDescription(String desc) {
        this.description = desc;
    }

    /**
     * gets the entry date of this item
     * @return item entry date
     */
    public Date getDateEntered() {
        return dateEntered;
    }

    /**
     * gets the expiration date of this item
     * @return item expiration date
     */
    public Date getDateExpired() {
        return dateExpired;
    }

    /**
     * sets the entry date of this item
     * @param dateEntered item entry date
     */
    public void setDateEntered(Date dateEntered) {
        this.dateEntered = dateEntered;
    }

    /**
     * sets the expiration date of this item
     * @param dateExpired item expiration date
     */
    public void setDateExpired(Date dateExpired) {
        this.dateExpired = dateExpired;
    }
}