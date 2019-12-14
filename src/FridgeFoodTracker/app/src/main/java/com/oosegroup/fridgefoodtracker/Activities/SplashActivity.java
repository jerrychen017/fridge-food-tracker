package com.oosegroup.fridgefoodtracker.Activities;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.oosegroup.fridgefoodtracker.R;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {
    String jsonString;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        this.pref = getSharedPreferences("fridge-food-tracker", MODE_PRIVATE);

        // checks if there exists a token and user is logged in
        if (this.pref.getString("token", null) != null
                && this.pref.getBoolean("loggedIn", true)) {
            System.out.println("splash: token exists");
            System.out.println("token is : " + this.pref.getString("token", null));
            // token is available, download fridge data and  go to MainActivity
//            downloadFridgeData(this.pref.getInt("fridge-id", -1));

            downloadFridgeData(0);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    goToMainActivity();
                }
            }, 500);
        } else {
            System.out.println("token doesn't exist");
            System.out.println("token is " + this.pref.getString("token", null));
            System.out.println("loggedIn is " + this.pref.getBoolean("loggedIn", true));
            // no token available, go to LoginActivity
            Intent loginActivityIntent = new Intent(this, LoginActivity.class);
            startActivity(loginActivityIntent);
        }
    }

    private void goToMainActivity() {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        mainActivityIntent.putExtra("fridgeDataTag", jsonString);
        startActivity(mainActivityIntent);
    }

    private String downloadFridgeData(int id) {
        try {
            String url = "http://oose-fridgetracker.herokuapp.com/fridge/" + id;
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Success Callback
                            System.out.println("Successfully posted an item");
                            jsonString = response.toString();
                            System.out.println(jsonString);
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

            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjReq);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("Exception occured when seding http request. Error: " + e.getMessage());
        }

        return this.jsonString;
    }
}
