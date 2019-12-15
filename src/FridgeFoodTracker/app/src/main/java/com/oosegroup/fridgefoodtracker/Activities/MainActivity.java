package com.oosegroup.fridgefoodtracker.Activities;
import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.android.volley.RequestQueue;
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

public class MainActivity extends AppCompatActivity {
    static Fridge fridge;
    public ItemListViewAdapter itemListViewAdapter;
    public ExpandableListView mainItemListView;
    public List<String> expandableListTitle;
    public HashMap<String, List<String>> detailsMap;
    RequestQueue queue;
    Button start_camera_button;
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

        setupNavDrawer(toolbar);

        this.sharedPreferences = getSharedPreferences("fridge-food-tracker", MODE_PRIVATE);
        this.editor = this.sharedPreferences.edit();

        this.queue = Volley.newRequestQueue(this);
        this.fridge = new Fridge(queue, sharedPreferences, 0);

        String fridgeDataString = getIntent().getExtras().getString("fridgeDataTag");
        try {
            if (fridgeDataString != null) {
                JSONObject jsonObject = new JSONObject(fridgeDataString);
                fridge.initFridge(jsonObject);
            } else {
                fridge.initFridge();
            }
        } catch (JSONException e) {
            System.out.println(e.toString());
            fridge.initFridge();
        }


        if(ItemListController.getControllerMainActivity() == null) {
            ItemListController.setControllerMainActivity(this);
        }
        ItemListController.buildExpandableListAdapter(this, this.fridge);
        this.notificationController = new NotificationController(this, this.fridge);
        sendNotifications();
    }

    private void setupNavDrawer(Toolbar toolbar) {

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("Fridge 1");
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName("Fridge 2");
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(0).withName("Logout");

        Drawer.OnDrawerItemClickListener onDrawerItemClickListener = new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                System.out.println(drawerItem);
                if (drawerItem.getIdentifier() == 0) {
                    System.out.println("logging out");
                    loggout();
                }
                return false;
            }
        };

        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(item1, new DividerDrawerItem(), item2, new DividerDrawerItem(), item3)
                .withOnDrawerItemClickListener(onDrawerItemClickListener)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void sendNotifications() {
        System.out.println("inside sending");
        NotificationManagerCompat notificationManger = this.notificationController.getManager();
        List<Notification> notifications = this.notificationController.getNotifications();
        int count = 1;
        for(Notification notification : notifications) {
            System.out.println("for loop for each notification");
            notificationManger.notify(count, notification);
            count++;
        }
        ItemListController.buildExpandableListAdapter(this, this.fridge);
    }



    public HashMap<String, List<String>>  createDetailsMap(Fridge fridge) {
        LinkedHashMap<String, List<String>> detailsMap = new LinkedHashMap<>();
        for(Item item : fridge.getContent().getItems()) {
            List<String> curr = new ArrayList<String>();
            curr.add("Date Entered: " + item.getDateEntered());
            curr.add("Date Expires: " + item.getDateExpired());
            detailsMap.put(item.getDescription(), curr);
        }
        return detailsMap;
    }

    public void enterManually(View view) {
        if(this.manualEntryFragment == null) {
            this.manualEntryFragment = ManualEntryFragment.newInstance();
        }
        this.manualEntryFragment.show(getSupportFragmentManager(),"add_photo_dialog_fragment");

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

        // EditText expirationDate = (EditText) view.findViewById(R.id.edit_item_date_input);
        // expirationDate.setText(currItem.getDateExpired().toString());
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

    public void loggout() {
        this.editor.clear();
        this.editor.commit();
        goToLoginActivity();
    }

    private void goToLoginActivity() {
        Intent loginActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(loginActivityIntent);
    }



    public static Fridge getFridge(){
        return fridge;
    }
}
