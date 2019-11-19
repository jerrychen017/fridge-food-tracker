package com.oosegroup.fridgefoodtracker.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Calendar;
import java.util.Date;
import java.util.Collections;

public class ItemHistory {
    private List<Item> items;

    public ItemHistory() {
        this.items = new ArrayList<>();
    }

    public void addItem(Item it) {
        items.add(it);
    }

    // for now add more it
    public List<Item> recommend() {
        List<Item> result = new ArrayList<>();

//        Date today = new Date();
        Calendar cal = Calendar.getInstance();
//        cal.setTime(today);
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

