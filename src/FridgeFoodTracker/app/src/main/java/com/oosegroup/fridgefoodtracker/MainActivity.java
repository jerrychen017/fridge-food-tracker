package com.oosegroup.fridgefoodtracker;

import android.app.DownloadManager;
import android.os.Bundle;

import com.google.android.gms.common.api.Response;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.Gravity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.oosegroup.fridgefoodtracker.models.*;

public class MainActivity extends AppCompatActivity {
    Fridge fridge;
    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            Connection connection = DriverManager.getConnection(ConnectionConstants.URL);
        } catch (SQLException e) {
            System.out.println("connection is foobar");
        }

        this.fridge = new Fridge();
        this.tableLayout = findViewById(R.id.tableLayout1);


        /*
        List<String> strings = new ArrayList<>();
        strings.add("ONE");
        strings.add("TWO");
        strings.add("THREE"); */
        buildTable(fridge, tableLayout);

    }

    public Item inputItem(int id, String str){
       Item item = new Item(id);
       Description description = new Description(item.getId());
       description.setDetails(str);
       item.setDescription(description);

       return item;
    }

    public void inputItem(View view) {
        EditText mEdit = (EditText) findViewById(R.id.item_text_input);
        String text = mEdit.getText().toString();
        Item item = inputItem(1, text);
        TableRow row = addRow(item);
        this.tableLayout.addView(row);
    }

    public TableRow addRow(Item item){
        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        row.setGravity(Gravity.CENTER);

        TextView textView = new TextView(this);
        textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setText(item.getDescription().getDetails());

        row.addView(textView);
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

        return super.onOptionsItemSelected(item);
    }

    public void buildTable(Fridge fridge, TableLayout tableLayout) {
        for (Item item : fridge.getContent().getItems()) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            row.setGravity(Gravity.CENTER);

            TextView textView = new TextView(this);
            textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            textView.setGravity(Gravity.CENTER);
            textView.setText(item.getDescription().getDetails());

            row.addView(textView);
            tableLayout.addView(row);
        }
    }
}
