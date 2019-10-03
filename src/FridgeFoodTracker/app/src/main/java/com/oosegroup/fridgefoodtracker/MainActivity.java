package com.oosegroup.fridgefoodtracker;
import android.os.Bundle;

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

import java.util.ArrayList;
import java.util.List;

import com.oosegroup.fridgefoodtracker.models.*;

public class MainActivity extends AppCompatActivity {
    Fridge fridge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.fridge = new Fridge();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action2", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                EditText mEdit = (EditText) findViewById(R.id.item_text_input);
                String text = mEdit.getText().toString();

                fridge.addItem(InputItem(1, text));
            }
        });

        TableLayout tableLayout = findViewById(R.id.tableLayout1);

        /*
        List<String> strings = new ArrayList<>();
        strings.add("ONE");
        strings.add("TWO");
        strings.add("THREE"); */
        buildTable(fridge, tableLayout);

    }

    public Item InputItem(int id, String str){
       Item item = new Item(id);
       Description description = new Description(item.getId());
       description.setDetails(str);
       item.setDescription(description);
       return item;
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
