package com.oosegroup.fridgefoodtracker.models;

import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.oosegroup.fridgefoodtracker.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A fridge that can store items locally or remotely
 */
public class Fridge {
    // fridge id.
    private int id;

    // a list of all items in the fridge
    public ItemList content;

    // request queue passed to the fridge so that fridge can talk to server
    private RequestQueue queue;

    /**
     * Determines if the fridge store all its items locally. If false, all items will be stored
     * the server as well.
     */
    private boolean isLocal;


    /**
     * A constructor that only initializes Fridge id. Only store items locally.
     * RequestQueue is set to null. If fridge is not a local fridge, RequestQueue has to be set later.
     *
     * @param id      Fridge ID
     * @param isLocal True if only store all items locally. Otherwise items will be
     */
    public Fridge(int id, boolean isLocal) {
        this.content = new ItemList();
        this.isLocal = isLocal;
        this.queue = null;
    }

    /**
     * A constructor that initializes RequestQueue and Fridge ID. It by default stores all items remotely
     *
     * @param queue A request queue of http requests
     * @param id    a number that represents the ID of fridge. On server (localhost for example),
     *              it can be accessed by http://10.0.2.2:3000/fridge/id (for running android emulator)
     *              or http://localhost:300/fridge/id
     */
    public Fridge(RequestQueue queue, int id) {
        this.isLocal = false;
        this.id = id;
        this.content = new ItemList();
        this.queue = queue;




    }

    /**
     * Retrieve all items from the server upon creating the fridge locally
     */
    public void initFridge() {
        try {
            String url = "http://10.0.2.2:3000/fridge/" + this.id;
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Success Callback
                            System.out.println("Successfully posted an item");
                            try {
                                JSONArray arr = response.getJSONArray("items");
                                for (int i = 0; i < arr.length(); i++) {
                                    content.addItem(new Item(arr.getJSONObject(i).getInt("id"), arr.getJSONObject(i).getString("item")));
                                }
                            } catch (JSONException e) {
                                System.out.println("Error: Response doesn't have an object mapped to \'body\'");
                            }
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("Exception occured when seding http request. Error: " + e.getMessage());
        }
    }

    /**
     * Add every item in the list to the content of this fridge
     *
     * @param list a list of items to be added
     * @throws IllegalArgumentException Cannot set request queue to a local fridge.
     *                                  Cannot set request queue if it was set already.
     */
    public void addItems(List<Item> list) throws IllegalArgumentException {
        for (Item i : list) {
            try {
                this.addItem(i);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException();
            }

        }
    }

    /**
     * Sets request queue if it hasn't been set yet.
     *
     * @param requestQueue RequestQueue that will be set to this fridge.
     * @throws IllegalArgumentException Cannot set request queue to a local fridge.
     *                                  Cannot set request queue if it was set already.
     */
    public void setRequestQueue(RequestQueue requestQueue) throws IllegalArgumentException {
        if (isLocal) {
            throw new IllegalArgumentException("A local fridge doesn't need Request Queue.");
        }
        if (queue != null) {
            throw new IllegalArgumentException("A Request Queue has already been set.");
        }

        this.queue = requestQueue;
    }

    /**
     * Add an item to the fridge. Add the item to the server as well if the fridge stores items remotely.
     * Item will be generated automatically either by the server or the fridge.
     *
     * @param item Item that needs to be added.
     * @throws IllegalArgumentException
     */
    public void addItem(final Item item) throws IllegalArgumentException {
        content.addItem(item);
        if (!isLocal && queue == null) {
            throw new IllegalStateException("Cannot add item to the server since the request queue hasn't been set yet");
        }

        try {
            String url = "http://10.0.2.2:3000/fridge/" + this.id;
            JSONObject postparams = new JSONObject();
            postparams.put("item", item.getDescription());
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, postparams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Success Callback
                            System.out.println("Successfully posted an item");
                            try {
                                item.setId(response.getInt("id"));
                            } catch (JSONException e) {
                                System.out.println("Error: Response doesn't have an object mapped to \'id\'");
                            }
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("Exception occured when seding http request. Error: " + e.getMessage());
        }

    }

    /**
     * Removes the item with given id from the content of the fridge
     * @param id item id
     * @throws IllegalArgumentException
     */
    public void remove(int id) throws IllegalArgumentException {
        content.removeItem(id);
        if (!isLocal && queue == null) {
            throw new IllegalStateException("Cannot add item to the server since the request queue hasn't been set yet");
        }

        try {
            String url = "http://10.0.2.2:3000/fridge/" + this.id;
            JSONObject postparams = new JSONObject();
            postparams.put("id", Integer.toString(id));
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE,
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("Exception occured when sending http request. Error: " + e.getMessage());
        }
    }

    /**
     * get a list of items that expired before today
     * @param today today's date
     * @return list of expired items
     */
    public List<Item> getExpired(Date today) {
        return content.getExpiredItems(today);
    }

    public ItemList getContent() {
        return content;
    }

    /**
     * Adds a map of <String, Integer> as items into the fridge
     * @param m target map of <String, Integer> where string is the item description and integer is the item count
     */
    public void addMapItems(Map<String, Integer> m){
        for (Map.Entry<String, Integer> entry : m.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                addItem(new Item(entry.getKey()));
            }
        }
    }
}


/*
Create a fridge
/

get a list of items
{
   "body": [

      {
         "id": "01",
         "item": "Herbert Schildt"
      },
      {
         "id": "02",
         "item": "Herbert Schildt"
      },
      {
         "id": "03",
         "item": "Herbert Schildt"
      },
   ]
}

Adding a new item
{
   "body": [
      {
         "item": "some_description"
      }
   ]
}

{
   "body": [
      {
         "id": "123"
      }
   ]
}

Update an item
{
   "body": [

      {
         "id": "01",
         "item": "Herbert Schildt"
      }
   ]
}

Delete an item
{
   "body": [

      {
         "id": "01"
      }
   ]
}
 */