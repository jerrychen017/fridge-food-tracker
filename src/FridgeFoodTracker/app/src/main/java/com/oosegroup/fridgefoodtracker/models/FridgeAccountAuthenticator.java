package com.oosegroup.fridgefoodtracker.models;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Account authenticator that sends login and register updates to the server
 */
public class FridgeAccountAuthenticator {
    static private String username;
    static private String password;
    static private RequestQueue queue;
    static private SharedPreferences pref;
    static private Editor editor;
    static private int mStatusCode; // status code that server sends back

    /**
     * Initialize the request queue and shared preferences
     *
     * @param reqQueue          a reference to request queue
     * @param sharedPreferences a reference to shared preferences
     */
    public static void init(RequestQueue reqQueue, SharedPreferences sharedPreferences) {
        queue = reqQueue;
        pref = sharedPreferences;
        editor = pref.edit();
    }

    /**
     * creates a new account with given username and password.
     *
     * @param usrName  given username
     * @param passWord given password
     */
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
                            System.out.println("createAccount: Successfully registered an account");
                            try {
                                // store token in SharedPreferences
                                editor.putString("token", response.getString("token"));
                                editor.putBoolean("registered", true);
                                editor.commit();
                            } catch (JSONException e) {
                                System.err.println("createAccount: exception occurred when " +
                                        "parsing JSONObject with message " + e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Failure Callback
                            System.out.println("createAccount: Failed to create an account with " +
                                    "error message" + error.getMessage());
                            editor.putBoolean("registered", false);
                            editor.commit();
                        }
                    });
            queue.add(jsonObjReq);
        } catch (Exception e) {
            System.err.println("createAccount: exception occurred when sending POST request" +
                    "with error message " + e.getMessage());
        }
    }

    /**
     * login to an account with given username and password
     *
     * @param usrName  given username
     * @param passWord given password
     */
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
                            System.out.println("login: Successfully logged in to an account");
                            try {
                                // store token in SharedPreferences
                                editor.putString("token", response.getString("token"));
                                editor.putBoolean("loggedIn", true);
                                editor.commit();
                            } catch (JSONException e) {
                                System.err.println("login: exception occurred when " +
                                        "parsing JSONObject with message " + e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Failure Callback
                            System.out.println("login: Failed to log in");
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
            System.err.println("login: exception occurred when sending POST request" +
                    " with error message " + e.getMessage());
        }
    }
}
