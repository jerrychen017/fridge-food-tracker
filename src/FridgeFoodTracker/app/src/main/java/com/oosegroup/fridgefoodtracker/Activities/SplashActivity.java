package com.oosegroup.fridgefoodtracker.Activities;

import android.content.Intent;
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
import com.oosegroup.fridgefoodtracker.models.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class SplashActivity extends AppCompatActivity {

    String jsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        downloadFridgeData(0);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {
                System.out.println("###########");
                System.out.println(jsonString);
                System.out.println("###########");
                goToMainActivity();
            }
        }, 1000 * 10);
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
