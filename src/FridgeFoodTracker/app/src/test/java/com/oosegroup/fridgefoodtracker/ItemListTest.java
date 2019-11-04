package com.oosegroup.fridgefoodtracker;

import com.oosegroup.fridgefoodtracker.models.Item;
import com.oosegroup.fridgefoodtracker.models.ItemList;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for Item class
 */
public class ItemListTest {
    private ItemList itemList;

    @Before
    public void initialize() {
        this.itemList = new ItemList();
    }

    @Test
    public void testAddRemoveItem() {
        Item item1 = new Item(1, "item1");
        Item item2 = new Item(2, "item2");
        this.itemList.addItem(item1);
        assertEquals(this.itemList.getItems().get(0).getId(), 1);
        this.itemList.addItem(item2);
        assertEquals(this.itemList.getItems().get(1).getId(), 2);
    }

    @Test
    public void testSortByExpiration() {
        this.addAssortedItems();
        this.itemList.sortByExpiration();

        Item prevItem = null;
        for (Item currItem : this.itemList.getItems()) {
            if (prevItem != null) {
                assertTrue(prevItem.getDateExpired().compareTo(currItem.getDateExpired()) <= 0);
            }
            prevItem = currItem;
        }
    }

    /**
     * Helper method for adding 'random' assorted items
     */
    private void addAssortedItems() {
        this.itemList.addItem(new Item(1, "item1"));
        this.itemList.addItem(new Item(2, "item2"));
        this.itemList.addItem(new Item(12, "snakes"));
        this.itemList.addItem(new Item(10, "cobras"));
    }
}