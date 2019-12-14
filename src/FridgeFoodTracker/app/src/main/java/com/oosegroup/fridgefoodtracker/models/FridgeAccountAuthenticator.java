package com.oosegroup.fridgefoodtracker.models;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FridgeAccountAuthenticator {
    static String username;
    static String password;
    static RequestQueue queue;
    static SharedPreferences pref;
    static Editor editor;
    static int[] fridgeIDs;
    private static int mStatusCode;

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
                            System.out.println("Successfully registered ");
                            System.out.println("register response is " + response.toString());
                            try {
                                // store token in SharedPreferences
                                editor.putString("token", response.getString("token"));
                                editor.putBoolean("registered", true);
                                editor.commit();
                            } catch (JSONException e) {
                                System.out.println("Error: Response doesn't have an object mapped to \'id\'");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Failure Callback
                            System.out.println("Error: User already exists when creating account");
                            System.out.println(error.getMessage());
                            editor.putBoolean("registered", false);
                            editor.commit();
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
                            System.out.println("Successfully logged in");
                            try {
                                // store token in SharedPreferences
                                System.out.println("login response is " + response.toString());
                                editor.putString("token", response.getString("token"));
//                                JSONArray fridgeIDsArray = response.getJSONArray("fridge");
//                                fridgeIDs = new int[fridgeIDsArray.length()];
//                                for (int i = 0; i < fridgeIDsArray.length(); i++) {
//                                    fridgeIDs[i] = fridgeIDsArray.getInt(i);
//                                }
//                                // store fridge-id in pref
//                                editor.putInt("fridge-id", fridgeIDs[0]);
                                editor.putBoolean("loggedIn", true);
                                editor.commit();
                            } catch (JSONException e) {
                                System.out.println("Error: Response doesn't have an object mapped to \'id\'");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Failure Callback
                            System.out.println("Failed to log in");
                            System.out.println(error.getMessage());
                            editor.putBoolean("loggedIn", false);
                            editor.commit();
                        }
                    }) {
                    @Override
                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse res) {
                    mStatusCode = res.statusCode;
                    return super.parseNetworkResponse(res);
                }
            };
            queue.add(jsonObjReq);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("Exception occured when seding http request. Error: " + e.getMessage());
        }
    }

    // authenticate current token
    // returns true if logged in
    // else return false
    public static void auth() {
        try {
            String url = "http://oose-fridgetracker.herokuapp.com/user/who";
            JSONObject postparams = new JSONObject();
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            /*
                            {"role", "fridge" []}
                            */
                            //Success Callback
                            System.out.println("User exists!");
                            // put fridge ID to SharedPreferences
                            editor.putBoolean("loggedIn", true);
                            editor.commit();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Failure Callback
                            System.out.println("User does not exist!");
                            // put fridge ID to SharedPreferences
                            editor.putBoolean("loggedIn", false);
                            editor.commit();
                        }
                    })

            {
                /** Passing some request headers* */
                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("Content-Type", "application/json");
                    headers.put("token", pref.getString("token", null));
                    return headers;
                }
            };
            queue.add(jsonObjReq);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("Exception occured when seding http request. Error: " + e.getMessage());
        }
    }

    public static void logout() {
        editor.clear();
        editor.commit();
    }
}
