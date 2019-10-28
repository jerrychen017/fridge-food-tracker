package com.oosegroup.fridgefoodtracker.Activities;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.Gravity;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.oosegroup.fridgefoodtracker.R;
import com.oosegroup.fridgefoodtracker.models.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Fridge fridge;
    TableLayout tableLayout;
    RequestQueue queue;
    Button start_camera_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.queue = Volley.newRequestQueue(this);
        this.fridge = new Fridge(queue);
        this.tableLayout = findViewById(R.id.tableLayout1);
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
    }

    public void inputItem(View view) {
        EditText mEdit = (EditText) findViewById(R.id.item_text_input);
        String text = mEdit.getText().toString();

        Item item = new Item(fridge.getContent().getItems().size(), text);
        this.fridge.addItem(item);
        TableRow row = addRow(item);
        this.tableLayout.addView(row);
    }

    public TableRow addRow(Item item){
        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        row.setGravity(Gravity.CENTER);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.items, null);
        TextView textView = view.findViewById(R.id.list_item_string);
        textView.setText(item.getDescription());

        row.addView(view);
        return row;
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
        }

        return super.onOptionsItemSelected(item);
    }
}
