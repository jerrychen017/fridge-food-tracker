package com.oosegroup.fridgefoodtracker.models;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;


public class Fridge {
  private ItemList content;
  private RequestQueue queue;

  public Fridge() {
    content = new ItemList();
  }

  public Fridge(RequestQueue queue) {
    content = new ItemList();
    this.queue = queue;
  }

  public boolean addItems(ItemList list) {
    return true;
  }

  /*
  Adds an item to this fridge, and also adds it to the server.
  */
  public void addItem(Item item) {
    content.addItem(item);
    if (queue != null) {
      try {
        String url ="http://10.0.2.2:3000/fridge/" + item.getId();
        JSONObject postparams = new JSONObject();
        postparams.put("item", item.getDescription());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, postparams,
                new Response.Listener<JSONObject>() {
                  @Override
                  public void onResponse(JSONObject response) {
                    //Success Callback
                    System.out.println("Successfully posted an item");
                  }
                },
                new Response.ErrorListener() {
                  @Override
                  public void onErrorResponse(VolleyError error) {
                    //Failure Callback
                    System.out.println("Failed to post an item");
                    System.out.println(error.getMessage());
                  }
                });
        queue.add(jsonObjReq);
      } catch (Exception e){
        System.out.println(e.getMessage());
      }
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

  public void sortByExpiration() {
    this.getContent().sortByExpiration();
  }
}