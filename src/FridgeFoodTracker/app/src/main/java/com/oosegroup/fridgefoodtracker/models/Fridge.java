package com.oosegroup.fridgefoodtracker.models;
import android.content.SharedPreferences;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A fridge that can store items locally and sends updates to the server
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
    private SharedPreferences pref;
    private int mStatusCode; // status code that server sends back

    /**
     * A constructor that initializes RequestQueue and Fridge ID.
     * It by default stores all items remotely
     *
     * @param queue A request queue of http requests
     * @param id    a number that represents the ID of fridge. On server (localhost for example),
     *              it can be accessed by http://10.0.2.2:3000/fridge/id (for running android emulator)
     *              or http://localhost:300/fridge/id
     */
    public Fridge(RequestQueue queue, SharedPreferences sharedPreferences, int id) {
        this.id = id;
        this.content = new ItemList();
        this.eaten = new ItemHistory();
        this.trashed = new ItemHistory();
        this.queue = queue;
        this.pref = sharedPreferences;
    }

    /**
     * Parse all fridge items from the given JSON object
     *
     * @param response JSON object downloaded in SplashActivity
     */
    public void initFridge(JSONObject response) {
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
                        } else if (ch != 'M') {
                            if (!finishM) {
                                expM += ch;
                            } else if (!finishD) {
                                expD += ch;
                            } else {
                                expY += ch;
                            }
                        }
                    }
                    expCal.set(Integer.parseInt(expY),
                            Integer.parseInt(expM) - 1,
                            Integer.parseInt(expD));
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
                        } else if (ch != 'M') {
                            if (!finishM) {
                                enterM += ch;
                            } else if (!finishD) {
                                enterD += ch;
                            } else {
                                enterY += ch;
                            }
                        }
                    }
                    enterCal.set(Integer.parseInt(enterY),
                            Integer.parseInt(enterM) - 1,
                            Integer.parseInt(enterD));
                    Date enterDate = enterCal.getTime();
                    it.setDateEntered(enterDate);
                }
                content.addItem(it);
            }
        } catch (JSONException e) {
            System.err.println("initFridge: exception occurred when parsing JSONObject with message "
                    + e.getMessage());
        }
    }

    /**
     * Parse all fridge items from the given JSON object
     *
     * @param response JSON object downloaded in SplashActivity
     */
    public void initHistory(JSONObject response) {
        try {
            JSONArray arr = response.getJSONArray("items");
            for (int i = 0; i < arr.length(); i++) {
                String reason = arr.getJSONObject(i).getString("item");
                Item it = new Item(arr.getJSONObject(i).getInt("id"),
                        arr.getJSONObject(i).getString("item"));
                if (reason.compareTo("eat") == 0) {
                    System.out.println("in eaten");
                    eaten.addItem(it);
                } else if (reason.compareTo("trash") == 0) {
                    trashed.addItem(it);
                } else {
                    System.err.println("initHistory: Item was neither trashed nor eaten");
                }
            }
        } catch (JSONException e) {
            System.err.println("initHistory:  exception occurred when parsing JSONObject " +
                    "with message " + e.getMessage());
        }
    }

    /**
     * Add an item to the fridge. Add the item to the server as well
     * if the fridge stores items remotely.
     * Item ID will be uniquely generated by the server
     *
     * @param item Item that needs to be added to the fridge.
     */
    public void addItem(final Item item) {
        content.addItem(item);
        if (queue == null) {
            throw new IllegalStateException("addItem: request queue is null");
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
                postparams.put("expiration", "M" + (calendar.get(Calendar.MONTH) + 1)
                        + "D" + calendar.get(Calendar.DATE) + "Y" + calendar.get(Calendar.YEAR));
            }
            // post enter date calendar
            Date enter = item.getDateEntered();
            if (enter != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(enter);
                postparams.put("enter", "M" + (calendar.get(Calendar.MONTH) + 1)
                        + "D" + calendar.get(Calendar.DATE)
                        + "Y" + calendar.get(Calendar.YEAR));
            }
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, postparams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Success Callback
                            System.out.println("addItem: Successfully added an item");
                            try {
                                item.setId(response.getInt("id"));
                            } catch (JSONException e) {
                                System.out.println("addItem: exception occurred when " +
                                        "parsing JSONObject with message "
                                        + e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Failure Callback
                            System.out.println("addItem: Failed to add an item with error message" +
                                    error.getMessage());
                        }
                    }) {
                /** Passing some request headers* */
                @Override
                public Map getHeaders() {
                    HashMap headers = new HashMap();
                    headers.put("Content-Type", "application/json");
                    headers.put("authorization", pref.getString("token", null));
                    return headers;
                }
            };
            queue.add(jsonObjReq);
        } catch (Exception e) {
            System.err.println("addItem: Exception occurred when sending POST request. Error: "
                    + e.getMessage());
        }
    }

    /**
     * Removes the item with given id from the content of the fridge
     * Add the item to trashed ItemHistory if it's trashed.
     * Add the item to eaten ItemHistory if it's eaten.
     * Server will add the item to history when it deletes the item
     *
     * @param id       item id
     * @param wasEaten whether the item was eaten
     */
    public void remove(int id, boolean wasEaten) {
        if (wasEaten) {
            eaten.addItem(content.getItem(id));
        } else {
            trashed.addItem(content.getItem(id));
        }
        content.removeItem(id);
        if (queue == null) {
            System.err.println("remove: request queue is null");
        }

        try {
            String url = "http://oose-fridgetracker.herokuapp.com/fridge/" + this.id + "/item/" + id;
            if (wasEaten) {
                url += "/eat";
            } else {
                url += "/trash";
            }

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE,
                    url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Success Callback
                            System.out.println("remove: Successfully deleted an item");
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Failure Callback
                            System.err.println("remove: Failed to delete an item with error message"
                                    + error.getMessage());
                        }
                    }) {
                /** Passing some request headers* */
                @Override
                public Map getHeaders() {
                    HashMap headers = new HashMap();
                    headers.put("Content-Type", "application/json");
                    headers.put("authorization", pref.getString("token", null));
                    return headers;
                }

                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse res) {
                    mStatusCode = res.statusCode;
                    return super.parseNetworkResponse(res);
                }
            };
            queue.add(jsonObjReq);
        } catch (Exception e) {
            System.err.println("remove: Exception occurred when sending DELETE request " +
                    "with error message " + e.getMessage());
        }
    }

    /**
     * Edit an item with the given item id by updating its description and expiration date
     *
     * @param id          target item id
     * @param description description to be updated to
     * @param dateExpired expiration date to be updated to
     */
    public void edit(int id, String description, Date dateExpired) {
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
                            System.out.println("edit: Successfully updated an item");
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Failure Callback
                            System.err.println("edit: Failed to update an item with error" +
                                    "message " + error.getMessage());
                        }
                    }) {
                /** Passing some request headers* */
                @Override
                public Map getHeaders() {
                    HashMap headers = new HashMap();
                    headers.put("Content-Type", "application/json");
                    headers.put("authorization", pref.getString("token", null));
                    return headers;
                }
            };
            queue.add(jsonObjReq);
        } catch (Exception e) {
            System.err.println("edit: Exception occurred when sending PUT request " +
                    "with error message " + e.getMessage());
        }
    }

    /**
     * get a list of items that expired before today
     *
     * @param today today's date
     * @return list of expired items
     */
    public List<Item> getExpired(Date today) {
        return content.getExpiredItems(today);
    }

    /**
     * gets the content of the fridge
     *
     * @return content of the fridge as an ItemList
     */
    public ItemList getContent() {
        return content;
    }

    /**
     * Sort current items in the fridge by their expiration dates with the item with
     * earliest expiration date at the front
     */
    public void sortByExpiration() {
        this.getContent().sortByExpiration();
    }

    /**
     * Sort current items in the fridge by their entry dates with the item with
     * earliest entry date at the front
     */
    public void sortByEntryDate() {
        this.getContent().sortByEntryDate();
    }

    /**
     * Recommend a list of items for the user to buy
     * @return list of items
     */
    public List<Item> recommend() {
        return this.eaten.recommend();
    }

    /**
     * gets the id of the fridge
     * @return id of this fridge
     */
    public int getID() {
        return this.id;
    }
}