package com.oosegroup.fridgefoodtracker;

import com.oosegroup.fridgefoodtracker.models.Fridge;
import com.oosegroup.fridgefoodtracker.models.Item;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for Item class
 */
public class FridgeTest {
    @Test
    public void testGetContent() {
        Fridge fridge = new Fridge(0, true);
        Item item1 = new Item(1, "item1");
        Item item2 = new Item(2, "item2");
        fridge.addItem(item1);
        fridge.addItem(item2);
        assertEquals(fridge.getContent().getItems().get(0).getId(), 1);
        assertEquals(fridge.getContent().getItems().get(1).getId(), 2);
    }
}