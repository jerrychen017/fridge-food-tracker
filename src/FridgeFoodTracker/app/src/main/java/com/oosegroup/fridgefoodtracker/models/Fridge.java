package com.oosegroup.fridgefoodtracker.models;

import com.oosegroup.fridgefoodtracker.ConnectionConstants;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Fridge {
  private ItemList content;
  //private Connection connection;

  public Fridge() {
    content = new ItemList();
    /*
    try {
      this.connection = DriverManager.getConnection(ConnectionConstants.URL);
      Statement statement = connection.createStatement();
      statement.execute("CREATE TABLE IF NOT EXISTS items (identifier INTEGER PRIMARY KEY AUTOINCREMENT, description TEXT)");
      statement.close();
    } catch (Exception e) {
      // do nothing
    } */
  }

  public boolean addItems(ItemList list) {
    return true;
  }

  public void addItem(Item item) {
    content.addItem(item);
    try {
      URL obj = new URL(ConnectionConstants.URL);
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();

      con.setDoOutput(true);
      con.setRequestMethod("POST");
      con.setRequestProperty("Content-Type", "application/json; utf-8");
      con.connect();

      OutputStream os = con.getOutputStream();
      OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
      osw.write(item.getDescription().getDetails());

      System.out.println(con.getResponseCode());
      System.out.println(con.getResponseMessage());
      osw.flush();
      osw.close();

    } /*catch (SQLException sqle) {
      // do nothing 3
    } */ catch (MalformedURLException e) {
      // do nothing
    } catch (IOException ioe) {
      // do nothing 2
    }
  }

  public void remove(int id) {
    content.removeItem(id);
  }

  /*
  returns a list of expired items
   */
  public ItemList getExpired() {

    return new ItemList();
  }

  public ItemList getContent() {
    return content;
  }
}