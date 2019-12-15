package com.oosegroup.fridgefoodtracker.models;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;

/**
 * Stores the history of items stored in the fridge.
 */
public class ItemHistory {
    private List<Item> items;

    /**
     * Constructor
     */
    public ItemHistory() {
        this.items = new ArrayList<>();
    }

    /**
     * add item to the history
     * @param it item to be added
     */
    public void addItem(Item it) {
        items.add(it);
    }

    /**
     * Recommend items for users to buy based on this history
     * @return a list of items
     */
    public List<Item> recommend() {
        List<Item> result = new ArrayList<>();

        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DATE, -7);
        Date weekBefore = cal.getTime();
        for (Item i : items) {
            if (i.getDateEntered().compareTo(weekBefore) > 0) {
                result.add(i);
            }
        }
        return result;
    }
}

