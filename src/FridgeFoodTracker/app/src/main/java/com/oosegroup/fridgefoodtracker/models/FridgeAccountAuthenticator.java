package com.oosegroup.fridgefoodtracker.models;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;


public class FridgeAccountAuthenticator {
    static String username;
    static String password;
    static RequestQueue queue;
    static SharedPreferences pref;
    static Editor editor;
    static int[] fridgeIDs;

    public static void init(RequestQueue reqQueue, SharedPreferences sharedPreferences) {
        queue = reqQueue;
        pref = sharedPreferences;
        editor = pref.edit();
    }

    public static void createAccount(String usrName, String passWord) {
        username = usrName;
        password = passWord;
        try {
            String url = "http://oose-fridgetracker.herokuapp.com/user/register";
            JSONObject postparams = new JSONObject();
            postparams.put("username", username);
            postparams.put("password", password);
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, postparams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Success Callback
                            System.out.println("Successfully posted an item");
                            try {
                                // store token in SharedPreferences
                                editor.putString("token", response.getString("token"));
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

    public static void login(String usrName, String passWord) {
        username = usrName;
        password = passWord;
        try {
            String url = "http://oose-fridgetracker.herokuapp.com/user/login";
            JSONObject postparams = new JSONObject();
            postparams.put("username", username);
            postparams.put("password", password);
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, postparams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Success Callback
                            System.out.println("Successfully posted an item");
                            try {
                                // store token in SharedPreferences
                                editor.putString("token", response.getString("token"));
                                JSONArray fridgeIDsArray = response.getJSONArray("fridge");
                                fridgeIDs = new int[fridgeIDsArray.length()];
                                for (int i = 0; i < fridgeIDsArray.length(); i++) {
                                    fridgeIDs[i] = fridgeIDsArray.getInt(i);
                                }
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

    // authenticate current token
    // returns true if logged in
    // else return false
    public static boolean auth() {
        return true;
    }
}
