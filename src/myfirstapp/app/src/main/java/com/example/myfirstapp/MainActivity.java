package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    private List<String> history;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        history = new ArrayList<>();
        tableLayout = findViewById(R.id.tableLayout1);
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        EditText editText = findViewById(R.id.editText);
        history.add(editText.getText().toString());

        TableRow tableRow = new TableRow(this);

        Item item = new Item(0, editText.getText().toString());

        TextView textView = new TextView(this);
        textView.setText(item.description);

        tableRow.addView(textView);
        tableLayout.addView(tableRow);

        /*
        StringBuilder sb = new StringBuilder();
        for (String s : history) {
            sb.append(s).append('\n');
        }

        TextView textView2 = findViewById(R.id.textView2);
        textView2.setText(sb.toString()); */
    }
}

