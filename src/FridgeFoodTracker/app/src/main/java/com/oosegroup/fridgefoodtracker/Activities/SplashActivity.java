package com.oosegroup.fridgefoodtracker.Activities;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.oosegroup.fridgefoodtracker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {
    String jsonStringData;
    String jsonStringHistory;
    String jsonFridgeIDs;
    SharedPreferences pref;
    Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        this.pref = getSharedPreferences("fridge-food-tracker", MODE_PRIVATE);
        this.editor = this.pref.edit();

        // checks if there exists a token and user is logged in
        if (this.pref.getString("token", null) != null
                && this.pref.getBoolean("loggedIn", false)) {
            System.out.println("splash: token exists");
            System.out.println("token is : " + this.pref.getString("token", null));
            // token is available, download fridge data and  go to MainActivity
            Handler handler = new Handler();
            if (pref.getBoolean("fridge-change", false)) {
                editor.remove("fridge-change");
                editor.putBoolean("fridge-change", false);
                editor.commit();
            } else {
                downloadFrdigeIDs();
            }
            handler.postDelayed(new Runnable() {
                public void run() {
                    if (pref.getInt("fridge-id_size", -1) != 0) {
//                        System.out.println("current fridge id is " + pref.getInt("fridge-id_cur", -1));
                        downloadFridgeData(pref.getInt("fridge-id_cur", -1));
                        downloadFridgeHistory(pref.getInt("fridge-id_cur", -1));
                    }
                }
            }, 500);
            handler.postDelayed(new Runnable() {
                public void run() {
                    goToMainActivity();
                }
            }, 500);
        } else {
            System.out.println("token doesn't exist");
            System.out.println("token is " + this.pref.getString("token", null));
            System.out.println("loggedIn is " + this.pref.getBoolean("loggedIn", false));
            // no token available, go to LoginActivity
            Intent loginActivityIntent = new Intent(this, LoginActivity.class);
            startActivity(loginActivityIntent);
        }
    }

    private void goToMainActivity() {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        mainActivityIntent.putExtra("fridgeDataTag", jsonStringData);
        mainActivityIntent.putExtra("fridgeHistoryTag", jsonStringHistory);
        startActivity(mainActivityIntent);
    }

    private void downloadFridgeData(int id) {
        try {
            String url = "http://oose-fridgetracker.herokuapp.com/fridge/" + id;
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Success Callback
                            System.out.println("Successfully posted an item");
                            jsonStringData = response.toString();
                            System.out.println(jsonStringData);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Failure Callback
                            System.out.println("Failed to post an item");
                            System.out.println(error.getMessage());
                        }
                    }) {
                /** Passing some request headers* */
                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("Content-Type", "application/json");
                    headers.put("authorization", pref.getString("token", null));
                    return headers;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjReq);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("Exception occured when seding http request. Error: " + e.getMessage());
        }
    }

    private void downloadFridgeHistory(int id) {
        // initializing history
        try {
            String url = "http://oose-fridgetracker.herokuapp.com/fridge/" + id + "/history";
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Success Callback
                            jsonStringHistory = response.toString();
                            System.out.println(jsonStringHistory);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Failure Callback
                            System.out.println("Failed to post an item");
                            System.out.println(error.getMessage());
                        }
                    }) {
                /** Passing some request headers* */
                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("Content-Type", "application/json");
                    headers.put("authorization", pref.getString("token", null));
                    return headers;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjReq);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("Exception occured when seding http request. Error: " + e.getMessage());
        }
    }

    private void downloadFrdigeIDs() {
        try {
            String url = "http://oose-fridgetracker.herokuapp.com/user/who";
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Success Callback
                            System.out.println("downloadfridgeIDs: successfully retrieved IDs");
                            try {
                                JSONArray fridgeIDsArray = response.getJSONArray("fridge");
                                // save IDs array to SharedPreferences
                                int numIDs = fridgeIDsArray.length();
                                editor.putInt("fridge-id_size", numIDs);
                                for (int i = 0; i < numIDs; i++) {
                                    editor.remove("fridge-id_" + i);
                                    editor.putInt("fridge-id_" + i, fridgeIDsArray.getInt(i));
                                }

                                // initialize current fridge-id to the first id in the array
                                if (numIDs != 0) {
                                    editor.remove("fridge-id_cur");
                                    editor.putInt("fridge-id_cur", fridgeIDsArray.getInt(0));
                                }

                                jsonFridgeIDs = response.toString();
                                System.out.println("fridge IDs are: " + jsonFridgeIDs);
                                editor.commit();
                            } catch (JSONException e) {
                                System.out.println("Error: cannot parse fridge id as int");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Failure Callback
                            System.out.println("downloadfridgeIDs: failed to retrieve IDs");
                            System.out.println(error.getMessage());
                        }
                    }) {
                /** Passing some request headers* */
                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("Content-Type", "application/json");
                    headers.put("authorization", pref.getString("token", null));
                    return headers;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjReq);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("Exception occured when seding http request. Error: " + e.getMessage());
        }
    }
}
