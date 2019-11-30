package com.oosegroup.fridgefoodtracker.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ItemList {
    private List<Item> items;
    private ItemHistory history;

    public ItemList(int size) {
        this.items = new ArrayList<Item>(size);
        this.history = new ItemHistory();
    }

    public ItemList() {
        this.items = new ArrayList<Item>();
        this.history = new ItemHistory();
    }

    public void addItem(Item it) {
        this.items.add(it);
    }

    /*
    removes the item with given id.
     */
    public void removeItem(int id) {
        for (Item i : items) {
            if (i.getId() == id) {
                items.remove(i);
                break;
            }
        }
    }

    public Item getItem(int id) {
        for (Item i : items) {
            if (i.getId() == id) {
                return i;
            }
        }
        return null;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<Item> getExpiredItems(Date today) {
        List<Item> result = new ArrayList<>();
        for (Item i: items) {
            if(i.getDateExpired().compareTo(today) < 0) { // item expired
                result.add(i);
            }
        }
        return result;
    }

    public void sortByExpiration() {
        Collections.sort(this.getItems(), new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o1.getDateExpired().compareTo(o2.getDateExpired());
            }
        });
    }

    public void sortByEntryDate() {
        Collections.sort(this.getItems(), new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o1.getDateEntered().compareTo(o2.getDateEntered());
            }
        });
    }

}