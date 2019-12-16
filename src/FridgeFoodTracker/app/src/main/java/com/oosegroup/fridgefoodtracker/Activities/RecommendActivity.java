package com.oosegroup.fridgefoodtracker.Activities;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Activity for generating a recommended shopping list
 */
public class RecommendActivity extends AppCompatActivity {
    ArrayAdapter<String> adapter;
    MainActivity mainActivity;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        this.sharedPreferences = getSharedPreferences("fridge-food-tracker", MODE_PRIVATE);
        this.editor = this.sharedPreferences.edit();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupNavDrawer(toolbar);

        Fridge fridge = mainActivity.getFridge();
        List<Item> eatenItems = fridge.recommend();
        List<String> eatenString = new ArrayList<>();
        for (Item i : eatenItems) {
            eatenString.add(i.getDescription());
        }
        adapter = new ArrayAdapter<String>(RecommendActivity.this, R.layout.recommend_listview, eatenString);
        ListView listView = (ListView) findViewById(R.id.recommendListView);
        listView.setAdapter(adapter);
    }

    private void setupNavDrawer(Toolbar toolbar) {
        PrimaryDrawerItem itemCreate = new PrimaryDrawerItem().withIdentifier(0).withName("Create A Fridge");
        PrimaryDrawerItem itemLogout = new PrimaryDrawerItem().withIdentifier(1).withName("Logout");
        PrimaryDrawerItem itemRecommend = new PrimaryDrawerItem().withIdentifier(2).withName("Recommendations");

        DrawerBuilder result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar);

        for (int i = 0; i < sharedPreferences.getInt("fridge-id_size", -1); i++) {
            result.addDrawerItems(new PrimaryDrawerItem().withIdentifier(i + 3).withName("Fridge " + (i + 1)), new DividerDrawerItem());
        }

        Drawer.OnDrawerItemClickListener onDrawerItemClickListener = new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                long identifier = drawerItem.getIdentifier();
                if (identifier == 1) {
                    logout();
                } else if (identifier == 0) {
                    createFridge();
                } else {
                    for (int i = 0; i < sharedPreferences.getInt("fridge-id_size", -1); i++) {
                        if (i + 3 == identifier) {
                            changeFridge(i);
                        }
                    }
                }
                return false;
            }
        };

        result.addDrawerItems(itemCreate, new DividerDrawerItem())
                .addDrawerItems(itemRecommend, new DividerDrawerItem())
                .addDrawerItems(itemLogout)
                .withOnDrawerItemClickListener(onDrawerItemClickListener);

        Drawer resultBuilt = result.build();
        resultBuilt.setSelection(2, false);
    }

    public void logout() {
        this.editor.clear();
        this.editor.commit();
        goToLoginActivity();
    }

    private void goToLoginActivity() {
        Intent loginActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(loginActivityIntent);
    }

    public void changeFridge(int index) {
        int id = sharedPreferences.getInt("fridge-id_" + index, -1);
        if (id == -1) {
            System.err.println("RecommendActivity changeFridge: error occurred when changing the fridge");
            return;
        }
        editor.remove("fridge-id_cur");
        editor.putInt("fridge-id_cur", id);
        editor.putBoolean("fridge-change", true);
        editor.commit();
        Intent splashActivityIntent = new Intent(RecommendActivity.this, SplashActivity.class);
        startActivity(splashActivityIntent);
    }

    public void createFridge() {
        try {
            String url = "http://oose-fridgetracker.herokuapp.com/user/f/new";
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Success Callback
                            System.out.println("RecommendActivity createFridge: successfully created a fridge");
                            try {
                                int id = response.getInt("id");
                                int size = sharedPreferences.getInt("fridge-id_size", -1);
                                if (size == -1) {
                                    System.err.println("RecommendActivity createFridge:" +
                                            " error occurred when creating a new fridge");
                                }
                                editor.remove("fridge-id_size");
                                editor.putInt("fridge-id_size", size + 1);
                                editor.putInt("fridge-id_" + size, id);
                                editor.remove("fridge-id_cur");
                                editor.putInt("fridge-id_cur", id);
                                editor.putBoolean("fridge-change", true);
                                editor.commit();
                                Intent splashActivityIntent = new Intent(RecommendActivity.this, SplashActivity.class);
                                startActivity(splashActivityIntent);
                            } catch (JSONException e) {
                                System.err.println("RecommendActivity createFridge: exception occurred " +
                                        "when parsing JSON string with error message " +
                                        e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Failure Callback
                            System.err.println("RecommendActivity createFridge: failed to create a fridge" +
                                    " with error message " + error.getMessage());
                        }
                    }) {
                /**
                 * Passing some request headers*
                 */
                @Override
                public Map getHeaders() {
                    HashMap headers = new HashMap();
                    headers.put("Content-Type", "application/json");
                    headers.put("authorization", sharedPreferences.getString("token", null));
                    return headers;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjReq);
        } catch (Exception e) {
            System.err.println("RecommendActivity createFridge: exception occurred when " +
                    "sending POST request with error message " + e.getMessage());
        }
    }
}

