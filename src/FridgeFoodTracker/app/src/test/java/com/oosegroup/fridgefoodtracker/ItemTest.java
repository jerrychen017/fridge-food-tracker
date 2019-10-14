package com.oosegroup.fridgefoodtracker;
import com.oosegroup.fridgefoodtracker.models.*;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Unit tests for Item class
 */
public class ItemTest {
    @Test
    public void itemOverloadingConstructor() {
        Item item1 = new Item(1);
        item1.setDescription("same description");
        Item item2 = new Item(2, "same description");
        assertEquals(item1.getDescription(), item2.getDescription());
    }

    @Test
    public void getExpirationDate() {
        Item item = new Item(1, "eggs");
        assertTrue(item.getDateExpired().after(item.getDateEntered()));
    }
}