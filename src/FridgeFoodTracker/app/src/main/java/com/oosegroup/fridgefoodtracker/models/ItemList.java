package com.oosegroup.fridgefoodtracker.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * custom itemlist class for holding our fridge items
 */
public class ItemList {

    // list of items
    private List<Item> items;

    // item history (eaten/trashed)
    private ItemHistory history;

    /**
     * constructor for creating new itemlist
     * @param size the number of items
     */
    public ItemList(int size) {
        this.items = new ArrayList<Item>(size);
        this.history = new ItemHistory();
    }

    /**
     * constructor for creating new itemlist
     */
    public ItemList() {
        this.items = new ArrayList<Item>();
        this.history = new ItemHistory();
    }

    /**
     * adds new item to list
     * @param it the item to add
     */
    public void addItem(Item it) {
        this.items.add(it);
    }

    /**
     * removes item from list
     * @param id the id of the item to be removed
     */
    public void removeItem(int id) {
        for (Item i : items) {
            if (i.getId() == id) {
                items.remove(i);
                break;
            }
        }
    }

    /**
     * returns the item from list
     * @param id the item to retrieve
     * @return the item from list
     */
    public Item getItem(int id) {
        for (Item i : items) {
            if (i.getId() == id) {
                return i;
            }
        }
        return null;
    }

    /**
     * returns the full item list
     * @return the full item list
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Returns the expired items in the list
     * @param today today's date to calclulate expired items
     * @return the expired items in the list
     */
    public List<Item> getExpiredItems(Date today) {
        List<Item> result = new ArrayList<>();
        for (Item i: items) {
            if(i.getDateExpired().compareTo(today) < 0) { // item expired
                result.add(i);
            }
        }
        return result;
    }

    /**
     * sorts the list by expiration date
     */
    public void sortByExpiration() {
        Collections.sort(this.getItems(), new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o1.getDateExpired().compareTo(o2.getDateExpired());
            }
        });
    }

    /**
     * sorts the list by the date the item was entered
     */
    public void sortByEntryDate() {
        Collections.sort(this.getItems(), new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o1.getDateEntered().compareTo(o2.getDateEntered());
            }
        });
    }

}