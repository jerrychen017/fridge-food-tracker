package com.example.myfirstapp;

public class Item {
    public int id;
    public String description;
    public int timeEntered = 0;
    public int timeExpired = 0;

    public Item(int id, String description) {
        this.id = id;
        this.description = description;
    }
}
