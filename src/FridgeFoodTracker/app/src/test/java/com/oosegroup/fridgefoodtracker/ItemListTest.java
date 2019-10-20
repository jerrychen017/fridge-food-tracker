package com.oosegroup.fridgefoodtracker;

import com.oosegroup.fridgefoodtracker.models.Item;
import com.oosegroup.fridgefoodtracker.models.ItemList;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for Item class
 */
public class ItemListTest {
    @Test
    public void testAddRemoveItem() {
        ItemList list = new ItemList();
        Item item1 = new Item(1, "item1");
        Item item2 = new Item(2, "item2");
        list.addItem(item1);
        assertEquals(list.getItems().get(0).getId(), 1);
        list.addItem(item2);
        assertEquals(list.getItems().get(1).getId(), 2);
    }
}