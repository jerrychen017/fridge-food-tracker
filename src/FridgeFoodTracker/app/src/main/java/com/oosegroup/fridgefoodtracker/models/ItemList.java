package com.oosegroup.fridgefoodtracker.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class ItemList {
  private List<Item> items;
  private ItemHistory history;

  public ItemList(int size) {
    this.items = new ArrayList<>(size);
    this.history = new ItemHistory();
  }

  public ItemList() {
    this.items = new ArrayList<>();
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

  public List<Item> getItems() {
    return items;
  }

  public Iterator createIterator() {
    return items.iterator();
  }

  public void sortByExpiration() {
    Collections.sort(this.getItems(), new Comparator<Item>() {
      @Override
      public int compare(Item o1, Item o2) {
        return o1.getDateExpired().compareTo(o2.getDateExpired());
      }
    });
  }

}