package com.oosegroup.fridgefoodtracker.Activities;
import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
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
import com.oosegroup.fridgefoodtracker.models.*;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    static Fridge fridge;
    public ItemListViewAdapter itemListViewAdapter;
    public ExpandableListView mainItemListView;
    public List<String> expandableListTitle;
    public HashMap<Integer, List<String>> detailsMap;
    RequestQueue queue;
    NotificationController notificationController;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ManualEntryFragment manualEntryFragment;
    EditEntryFragment editEntryFragment;
    public String sortingState = "NONE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.sharedPreferences = getSharedPreferences("fridge-food-tracker", MODE_PRIVATE);
        this.editor = this.sharedPreferences.edit();

        setupNavDrawer(toolbar);

        this.queue = Volley.newRequestQueue(this);
        this.fridge = new Fridge(queue, sharedPreferences, this.sharedPreferences.getInt("fridge-id_cur", -1));

        String fridgeDataString = getIntent().getExtras().getString("fridgeDataTag");
        String fridgeHistoryString = getIntent().getExtras().getString("fridgeHistoryTag");
        try {
            if (fridgeDataString != null) {
                JSONObject jsonObject = new JSONObject(fridgeDataString);
                fridge.initFridge(jsonObject);
            } else {
                fridge.initFridge();
            }
        } catch (JSONException e) {
            System.err.println("MainActivity onCreate: exception occurred when constructing " +
                    "JSON object with error message " + e.getMessage());
        }

        try {
            if (fridgeHistoryString != null) {
                JSONObject jsonObject = new JSONObject(fridgeHistoryString);
                fridge.initHistory(jsonObject);
            } else {
                fridge.initHistory();
            }
        } catch (JSONException e) {
            System.err.println("MainActivity onCreate: exception occurred when constructing " +
                    "JSON object with error message " + e.getMessage());
        }

        if (ItemListController.getControllerMainActivity() == null) {
            ItemListController.setControllerMainActivity(this);
        }

        if (ItemListController.getControllerFridge() == null) {
            ItemListController.setControllerFridge(this.fridge);
        }

        ItemListController.buildExpandableListAdapter(this, this.fridge);
        this.notificationController = new NotificationController(this, this.fridge);
        sendNotifications();
    }

    private void setupNavDrawer(Toolbar toolbar) {
        PrimaryDrawerItem itemCreate = new PrimaryDrawerItem().withIdentifier(0).withName("Create A Fridge");
        PrimaryDrawerItem itemLogout = new PrimaryDrawerItem().withIdentifier(1).withName("Logout");
        PrimaryDrawerItem itemRecommend = new PrimaryDrawerItem().withIdentifier(2).withName("Recommendations");
        long curIdentifier = 0;

        DrawerBuilder result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar);

        for (int i = 0; i < sharedPreferences.getInt("fridge-id_size", -1); i++) {
            result.addDrawerItems(new PrimaryDrawerItem().withIdentifier(i + 3).withName("Fridge " + (i + 1)), new DividerDrawerItem());
            if (sharedPreferences.getInt("fridge-id_" + i, -1) == sharedPreferences.getInt("fridge-id_cur", -1)) {
                curIdentifier = i + 3;
            }
        }

        if (curIdentifier - 2 < 0) {
            setTitle("Create a fridge from Sidebar!");
        } else {
            setTitle("Fridge " + (curIdentifier - 2));
        }
        Drawer.OnDrawerItemClickListener onDrawerItemClickListener = new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                long identifier = drawerItem.getIdentifier();
                if (identifier == 1) {
                    logout();
                } else if (identifier == 0) {
                    createFridge();
                } else if (identifier == 2) {
                    goToRecommendActivity();
                } else {
                    int tempID = -1;
                    for (int i = 0; i < sharedPreferences.getInt("fridge-id_size", -1); i++) {
                        if (sharedPreferences.getInt("fridge-id_" + i, -1) == sharedPreferences.getInt("fridge-id_cur", -1)) {
                            tempID = i + 3;
                        }
                        if (tempID == identifier) {
                            return false;
                        }
                    }

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
        resultBuilt.setSelection(curIdentifier, false);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public void sendNotifications() {
        NotificationManagerCompat notificationManger = this.notificationController.getManager();
        List<Notification> notifications = this.notificationController.getNotifications();
        int count = 1;
        for (Notification notification : notifications) {
            notificationManger.notify(count, notification);
            count++;
        }
        ItemListController.buildExpandableListAdapter(this, this.fridge);
    }


    public HashMap<Integer, List<String>> createDetailsMap(Fridge fridge) {
        LinkedHashMap<Integer, List<String>> detailsMap = new LinkedHashMap<>();
        for (Item item : fridge.getContent().getItems()) {
            List<String> curr = new ArrayList<String>();
            curr.add("Date Entered: " + item.getDateEntered());
            curr.add("Date Expires: " + item.getDateExpired());
            detailsMap.put(item.getId(), curr);
        }
        return detailsMap;
    }

    public void enterManually(View view) {
        if (this.manualEntryFragment == null) {
            this.manualEntryFragment = ManualEntryFragment.newInstance();
        }
        this.manualEntryFragment.show(getSupportFragmentManager(), "add_photo_dialog_fragment");

    }

    public void inputItem(View view) {
        ItemListController.inputItem(this.manualEntryFragment.getView(), fridge, this);
    }

    public void eatItem(View view) {
        ItemListController.eatItem(view, fridge, this);
    }

    public void trashItem(View view) {
        ItemListController.trashItem(view, fridge, this);
    }

    public void enterEditDetails(View view) {
        this.editEntryFragment = EditEntryFragment.newInstance(Integer.parseInt(view.getTag(R.id.TAG_ID).toString()),
                (String) view.getTag(R.id.TAG_DESCRIPTION),
                (Date) view.getTag(R.id.TAG_EXP_DATE));

        this.editEntryFragment.show(getSupportFragmentManager(), "edit_item_dialog_fragment");

    }

    public void editItem(View view) {
        ItemListController.editItem(this.editEntryFragment.getView(),
                view,
                fridge, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_sortByExpiration) {
            this.sortingState = "EXPIRATION";
            this.fridge.sortByExpiration();
            ItemListController.buildExpandableListAdapter(this, this.fridge);
        }

        if (id == R.id.action_sortByEntryDate) {
            this.sortingState = "ENTER";
            this.fridge.sortByEntryDate();
            ItemListController.buildExpandableListAdapter(this, this.fridge);
        }

        return super.onOptionsItemSelected(item);
    }

    public void logout() {
        this.editor.clear();
        this.editor.commit();
        goToLoginActivity();
    }

    private void goToRecommendActivity() {
        Intent recommendActivityIntent = new Intent(this, RecommendActivity.class);
        startActivity(recommendActivityIntent);
    }

    private void goToLoginActivity() {
        Intent loginActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(loginActivityIntent);
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
                            System.out.println("MainActivity createFridge: successfully created a fridge");
                            try {
                                int id = response.getInt("id");
                                int size = sharedPreferences.getInt("fridge-id_size", -1);
                                if (size == -1) {
                                    System.err.println("MainActivity createFridge:" +
                                            " error occurred when creating a new fridge");
                                }
                                editor.remove("fridge-id_size");
                                editor.putInt("fridge-id_size", size + 1);
                                editor.putInt("fridge-id_" + size, id);
                                editor.remove("fridge-id_cur");
                                editor.putInt("fridge-id_cur", id);
                                editor.putBoolean("fridge-change", true);
                                editor.commit();
                                Intent splashActivityIntent = new Intent(MainActivity.this, SplashActivity.class);
                                startActivity(splashActivityIntent);
                            } catch (JSONException e) {
                                System.err.println("MainActivity createFridge: exception occurred " +
                                        "when parsing JSON string with error message " +
                                        e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Failure Callback
                            System.err.println("MainActivity createFridge: failed to create a fridge" +
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
            queue.add(jsonObjReq);
        } catch (Exception e) {
            System.err.println("MainActivity createFridge: exception occurred when " +
                    "sending POST request with error message " + e.getMessage());
        }
    }

    public static Fridge getFridge() {
        return fridge;
    }

    public void changeFridge(int index) {
        int id = sharedPreferences.getInt("fridge-id_" + index, -1);
        if (id == -1) {
            System.err.println("MainActivity changeFridge: error occurred when changing the fridge");
            return;
        }
        editor.remove("fridge-id_cur");
        editor.putInt("fridge-id_cur", id);
        editor.putBoolean("fridge-change", true);
        editor.commit();
        Intent splashActivityIntent = new Intent(MainActivity.this, SplashActivity.class);
        startActivity(splashActivityIntent);
    }
}
