package com.oosegroup.fridgefoodtracker.models;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.oosegroup.fridgefoodtracker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
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

    // stores all trashed items
    private ItemHistory trashed;

    // stores all eaten items
    private ItemHistory eaten;



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
        this.eaten = new ItemHistory();
        this.trashed = new ItemHistory();
        this.isLocal = isLocal;
        this.queue = null;
        this.id = id;
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
        this.eaten = new ItemHistory();
        this.trashed = new ItemHistory();
        this.queue = queue;
    }

    /**
     * Retrieve all items from the server upon creating the fridge locally
     */
    public void initFridge(JSONObject response) {
        // initialize item list
        try {
            System.out.println("Successfully posted an item");
            try {
                JSONArray arr = response.getJSONArray("items");
                for (int i = 0; i < arr.length(); i++) {
                    Item it = new Item(arr.getJSONObject(i).getInt("id"),
                            arr.getJSONObject(i).getString("item"));
                    // retrieve expiration date
                    String expStr = arr.getJSONObject(i).getString("expiration");
                    if (expStr != "null" && expStr != null) {
                        Calendar expCal = Calendar.getInstance();
                        boolean finishM = false;
                        String expM = new String();
                        boolean finishD = false;
                        String expD = new String();
                        String expY = new String();
                        for (int j = 0; j < expStr.length(); j++) {
                            char ch = expStr.charAt(j);
                            if (ch == 'D') {
                                finishM = true;
                            } else if (ch == 'Y') {
                                finishD = true;
                            } else if (ch != 'M'){
                                if (!finishM) {
                                    expM += ch;
                                } else if (!finishD){
                                    expD += ch;
                                } else {
                                    expY += ch;
                                }
                            }
                        }
                        expCal.set(Integer.parseInt(expY), Integer.parseInt(expM)-1, Integer.parseInt(expD));
                        Date expDate = expCal.getTime();
                        it.setDateExpired(expDate);
                    }

                    // retrieve enter date
                    String enterStr = arr.getJSONObject(i).getString("enter");
                    if (enterStr != "null" && enterStr != null) {
                        Calendar enterCal = Calendar.getInstance();
                        boolean finishM = false;
                        String enterM = new String();
                        boolean finishD = false;
                        String enterD = new String();
                        String enterY = new String();
                        for (int j = 0; j < enterStr.length(); j++) {
                            char ch = enterStr.charAt(j);
                            if (ch == 'D') {
                                finishM = true;
                            } else if (ch == 'Y') {
                                finishD = true;
                            } else if (ch != 'M'){
                                if (!finishM) {
                                    enterM += ch;
                                } else if (!finishD){
                                    enterD += ch;
                                } else {
                                    enterY += ch;
                                }
                            }
                        }
                        enterCal.set(Integer.parseInt(enterY), Integer.parseInt(enterM)-1, Integer.parseInt(enterD));
                        Date enterDate = enterCal.getTime();
                        it.setDateEntered(enterDate);
                    }


                    content.addItem(it);

                }
            } catch (JSONException e) {
                System.out.println("Error: Response doesn't have an object mapped to \'body\'");
            }

        } catch (Exception e) {
            System.out.println("ERROR");
        }

        /*
        // initializing history
        try {
            String url = "http://oose-fridgetracker.herokuapp.com/fridge/" + this.id + "/history";
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Success Callback
                            try {
                                JSONArray arr = response.getJSONArray("items");
                                for (int i = 0; i < arr.length(); i++) {
                                    String reason = arr.getJSONObject(i).getString("item");
                                    Item it = new Item(arr.getJSONObject(i).getInt("id"), arr.getJSONObject(i).getString("item"));
                                    if (reason.compareTo("eat") == 0) {
                                        eaten.addItem(it);
                                    } else if (reason.compareTo("trash") == 0) {
                                        trashed.addItem(it);
                                    } else {
                                        System.out.println("Item was neither trashed nor eaten");
                                    }
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

         */
    }

    /**
     * Retrieve all items from the server upon creating the fridge locally
     */
    public void initFridge() {
        // initialize item list
        try {
            String url = "http://oose-fridgetracker.herokuapp.com/fridge/" + this.id;
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
                                    Item it = new Item(arr.getJSONObject(i).getInt("id"),
                                            arr.getJSONObject(i).getString("item"));
                                    // retrieve expiration date
                                    String expStr = arr.getJSONObject(i).getString("expiration");
                                    if (expStr != "null" && expStr != null) {
                                        Calendar expCal = Calendar.getInstance();
                                        boolean finishM = false;
                                        String expM = new String();
                                        boolean finishD = false;
                                        String expD = new String();
                                        String expY = new String();
                                        for (int j = 0; j < expStr.length(); j++) {
                                            char ch = expStr.charAt(j);
                                            if (ch == 'D') {
                                                finishM = true;
                                            } else if (ch == 'Y') {
                                                finishD = true;
                                            } else if (ch != 'M'){
                                                if (!finishM) {
                                                    expM += ch;
                                                } else if (!finishD){
                                                    expD += ch;
                                                } else {
                                                    expY += ch;
                                                }
                                            }
                                        }
                                        expCal.set(Integer.parseInt(expY), Integer.parseInt(expM)-1, Integer.parseInt(expD));
                                        Date expDate = expCal.getTime();
                                        it.setDateExpired(expDate);
                                    }

                                    // retrieve enter date
                                    String enterStr = arr.getJSONObject(i).getString("enter");
                                    if (enterStr != "null" && enterStr != null) {
                                        Calendar enterCal = Calendar.getInstance();
                                        boolean finishM = false;
                                        String enterM = new String();
                                        boolean finishD = false;
                                        String enterD = new String();
                                        String enterY = new String();
                                        for (int j = 0; j < enterStr.length(); j++) {
                                            char ch = enterStr.charAt(j);
                                            if (ch == 'D') {
                                                finishM = true;
                                            } else if (ch == 'Y') {
                                                finishD = true;
                                            } else if (ch != 'M'){
                                                if (!finishM) {
                                                    enterM += ch;
                                                } else if (!finishD){
                                                    enterD += ch;
                                                } else {
                                                    enterY += ch;
                                                }
                                            }
                                        }
                                        enterCal.set(Integer.parseInt(enterY), Integer.parseInt(enterM)-1, Integer.parseInt(enterD));
                                        Date enterDate = enterCal.getTime();
                                        it.setDateEntered(enterDate);
                                    }


                                    content.addItem(it);

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

        // initializing history
        try {
            String url = "http://oose-fridgetracker.herokuapp.com/fridge/" + this.id + "/history";
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Success Callback
                            try {
                                JSONArray arr = response.getJSONArray("items");
                                for (int i = 0; i < arr.length(); i++) {
                                    String reason = arr.getJSONObject(i).getString("item");
                                    Item it = new Item(arr.getJSONObject(i).getInt("id"), arr.getJSONObject(i).getString("item"));
                                    if (reason.compareTo("eat") == 0) {
                                        eaten.addItem(it);
                                    } else if (reason.compareTo("trash") == 0) {
                                        trashed.addItem(it);
                                    } else {
                                        System.out.println("Item was neither trashed nor eaten");
                                    }
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
    public boolean addItem(final Item item) throws IllegalArgumentException {
        content.addItem(item);
        // TODO: change error checking
        if (!isLocal && queue == null) {
            throw new IllegalStateException("Cannot add item to the server since the request queue hasn't been set yet");
        }

        if (isLocal) {
            return false;
        }

        try {
            String url = "http://oose-fridgetracker.herokuapp.com/fridge/" + this.id;
            JSONObject postparams = new JSONObject();
            postparams.put("item", item.getDescription());
            // post expiration date calendar
            Date expiration = item.getDateExpired();
            if (expiration != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(expiration);
                postparams.put("expiration", "M"+(calendar.get(Calendar.MONTH)+1)
                        + "D"+calendar.get(Calendar.DATE)+"Y"+calendar.get(Calendar.YEAR));
            }
            // post enter date calendar
            Date enter = item.getDateEntered();
            if (enter != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(enter);
                postparams.put("enter", "M"+(calendar.get(Calendar.MONTH)+1)
                        + "D"+calendar.get(Calendar.DATE)
                        +"Y"+calendar.get(Calendar.YEAR));
            }
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
        return true;
    }

    /**
     * Removes the item with given id from the content of the fridge
     * @param id item id
     * @param wasEaten if the item was eaten
     * @throws IllegalArgumentException
     */
    public void remove(int id, boolean wasEaten) throws IllegalArgumentException {
        System.out.println("Called remove here with id " + id); // debug
        if (wasEaten) {
            eaten.addItem(content.getItem(id));
        } else {
            trashed.addItem(content.getItem(id));
        }
        content.removeItem(id);
        if (!isLocal && queue == null) {
            throw new IllegalStateException("Cannot add item to the server since the request queue hasn't been set yet");
        }

        if (isLocal) {
            return;
        }

        try {
            String url;
            if (wasEaten) {
                url = "http://oose-fridgetracker.herokuapp.com/fridge/" + this.id + "/item/" + id + "/eat";
            } else {
                url = "http://oose-fridgetracker.herokuapp.com/fridge/" + this.id + "/item/" + id + "/trash";
            }

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE,
                    url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Success Callback
                            System.out.println("Successfully deleted an item");
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Failure Callback
                            System.out.println("Failed to delete an item");
                            System.out.println(error.getMessage());
                        }
                    });
            queue.add(jsonObjReq);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("Exception occured when sending http DELETE request. Error: " + e.getMessage());
        }
    }

    public void edit(int id, String description, Date dateExpired) {
        // TODO: need to change item in server instead of just locally

        Item currItem = this.getContent().getItem(id);
        currItem.setDescription(description);
        currItem.setDateExpired(dateExpired);

        try {
            String url = "http://oose-fridgetracker.herokuapp.com/fridge/";
            JSONObject postparams = new JSONObject();
            postparams.put("id", currItem.getId());
            postparams.put("item", description);
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                    url, postparams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Success Callback
                            System.out.println("Successfully updated an item");
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Failure Callback
                            System.out.println("Failed to updated an item");
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

    public void sortByExpiration() {
        this.getContent().sortByExpiration();
    }

    public void sortByEntryDate() {
        this.getContent().sortByEntryDate();
    }

  public List<Item> recommend() {
        return this.eaten.recommend();
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


signup /user/register

signin
/user/login
"username"
"password"

/user/who
{"role", "fridge" []}

// set the header "authrization" with token


 */