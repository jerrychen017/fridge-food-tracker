package com.oosegroup.fridgefoodtracker.models;

import java.util.Map;

/*
Diet class
 */
public class Diet {
  private Map<String, Double> constrains;

  public boolean fitsDiet(ItemList list) {
    return true;
  }
}