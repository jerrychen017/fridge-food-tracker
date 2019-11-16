package com.oosegroup.fridgefoodtracker.Activities;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.Gravity;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.oosegroup.fridgefoodtracker.models.ProgressBar;
import com.oosegroup.fridgefoodtracker.R;
import com.oosegroup.fridgefoodtracker.models.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Fridge fridge;
    ItemListViewAdapter itemListViewAdapter;
    ExpandableListView mainItemListView;
    List<String> expandableListTitle;
    HashMap<String, List<String>> detailsMap;
    RequestQueue queue;
    Button start_camera_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.queue = Volley.newRequestQueue(this);

        this.fridge = new Fridge(queue, 0);
        fridge.initFridge();


        this.start_camera_button = (Button) findViewById(R.id.start_camera_button);
        // Capture button clicks
        start_camera_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(MainActivity.this,
                        CameraActivity.class);
                startActivity(myIntent);
            }
        });

        buildExpandableListAdapter(this, this.fridge);
    }

    public void buildExpandableListAdapter(Context context, Fridge fridge) {
        Log.d("adapter", "buildExpandableListAdapter: here");
        this.mainItemListView = findViewById(R.id.mainItemListView);
        this.detailsMap = createDetailsMap(fridge);
        this.expandableListTitle = new ArrayList<String>(detailsMap.keySet());
        this.itemListViewAdapter = new ItemListViewAdapter(this, fridge, this.expandableListTitle, this.detailsMap);
        this.mainItemListView.setAdapter(itemListViewAdapter);

    }

    public HashMap<String, List<String>>  createDetailsMap(Fridge fridge) {
        HashMap<String, List<String>> detailsMap = new HashMap<String, List<String>>();
        for(Item item : fridge.getContent().getItems()) {
            List<String> curr = new ArrayList<String>();
            curr.add("Date Entered: " + item.getDateEntered());
            curr.add("Date Expires: " + item.getDateExpired());
            detailsMap.put(item.getDescription(), curr);
        }
        return detailsMap;
    }

    public void inputItem(View view) {
        EditText mEdit = (EditText) findViewById(R.id.item_text_input);
        EditText dEdit = (EditText) findViewById(R.id.item_date_input);
        String text = mEdit.getText().toString();
        String date_s = dEdit.getText().toString();
        Date date;

        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(date_s);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            date = null;
            Log.i("invalid date", e.toString());
        }
        Item item;
        if (date != null) {
            item = new Item(fridge.getContent().getItems().size(), text, date);
        } else {
            item = new Item(fridge.getContent().getItems().size(), text);
        }

        this.fridge.addItem(item);

        /*
        this.itemListViewAdapter = new ItemListViewAdapter(this, this.fridge);
        this.mainItemListView = findViewById(R.id.mainItemListView);
        this.mainItemListView.setAdapter(itemListViewAdapter);
        */

        buildExpandableListAdapter(this, this.fridge);

        mEdit.setText("");
        dEdit.setText("");
    }

    public void deleteItem(View view) {
        this.fridge.remove(Integer.parseInt(view.getTag().toString()));

        /*
        this.itemListViewAdapter = new ItemListViewAdapter(this, this.fridge);
        this.mainItemListView = findViewById(R.id.mainItemListView);
        this.mainItemListView.setAdapter(itemListViewAdapter);

         */
        buildExpandableListAdapter(this, this.fridge);
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
            this.fridge.sortByExpiration();

            /*
            this.itemListViewAdapter = new ItemListViewAdapter(this, this.fridge);
            this.mainItemListView = findViewById(R.id.mainItemListView);
            this.mainItemListView.setAdapter(itemListViewAdapter);

             */
            buildExpandableListAdapter(this, this.fridge);
        }

        return super.onOptionsItemSelected(item);
    }
}
