package com.oosegroup.fridgefoodtracker.Activities;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.oosegroup.fridgefoodtracker.R;
import com.oosegroup.fridgefoodtracker.models.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Fridge fridge;
    public ItemListViewAdapter itemListViewAdapter;
    public ExpandableListView mainItemListView;
    public List<String> expandableListTitle;
    public HashMap<String, List<String>> detailsMap;
    RequestQueue queue;
    Button start_camera_button;
    NotificationController notificationController;

    ManualEntryFragment manualEntryFragment;
    EditEntryFragment editEntryFragment;
    public String sortingState = "NONE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.queue = Volley.newRequestQueue(this);

        this.fridge = new Fridge(queue, 0);
        fridge.initFridge();




        this.notificationController = new NotificationController(this, this.fridge);
        sendNotifications();
    }


    public void sendNotifications() {
        System.out.println("inside sending");
        NotificationManagerCompat notificationManger = this.notificationController.getManager();
        List<Notification> notifications = this.notificationController.getNotifications();
        for(Notification notification : notifications) {
            System.out.println("for loop for each notification");
            notificationManger.notify(1, notification);
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

    public void deleteItem(View view) {
        ItemListController.deleteItem(view, fridge, this);
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
}
