package com.oosegroup.fridgefoodtracker.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.oosegroup.fridgefoodtracker.R;
import com.oosegroup.fridgefoodtracker.models.Fridge;
import com.oosegroup.fridgefoodtracker.models.Item;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    MainActivity mainActivity;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String jsonString;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        this.pref = getSharedPreferences("fridge-food-tracker", MODE_PRIVATE);
        this.editor = this.pref.edit();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupNavDrawer(toolbar);


        Fridge fridge = mainActivity.getFridge();
        List<Item> eatenItems = fridge.recommend();
        List<String> eatenString = new ArrayList<>();
        for (Item i: eatenItems) {
            eatenString.add(i.getDescription());
        }
        adapter = new ArrayAdapter<String>(RecommendActivity.this, R.layout.recommend_listview, eatenString);
        ListView listView = (ListView) findViewById(R.id.recommendListView);
        listView.setAdapter(adapter);
    }

    private void setupNavDrawer(Toolbar toolbar) {

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Fridge 1");
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName("Fridge 2");
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName("Recommendations");
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName("Logout");

        Drawer.OnDrawerItemClickListener onDrawerItemClickListener = new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                System.out.println(drawerItem);
                if (drawerItem.getIdentifier() == 3) {
                    System.out.println("logging out");
                    logout();
                } else if (drawerItem.getIdentifier() == 1) {
                    updateData();
                }
                return false;
            }
        };

        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(item1, new DividerDrawerItem(), item2, new DividerDrawerItem(), item4, new DividerDrawerItem(), item3)
                .withOnDrawerItemClickListener(onDrawerItemClickListener)
                .build();
    }



    public void logout() {
        this.editor.clear();
        this.editor.commit();
        goToLoginActivity();
    }

    private void updateData() {

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

        Intent mainActivityIntent = new Intent (this, MainActivity.class);
        mainActivityIntent.putExtra("fridgeDataTag", this.jsonString);
        startActivity(mainActivityIntent);
    }

    private void goToLoginActivity() {
        Intent loginActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(loginActivityIntent);
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

        return this.jsonString;
    }
}

