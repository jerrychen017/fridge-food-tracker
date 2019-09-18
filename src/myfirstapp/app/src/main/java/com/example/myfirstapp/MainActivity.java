package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    private List<String> history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        history = new ArrayList<>();
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        EditText editText = findViewById(R.id.editText);
        history.add(editText.getText().toString());

        StringBuilder sb = new StringBuilder();
        for (String s : history) {
            sb.append(s).append('\n');
        }

        TextView textView2 = findViewById(R.id.textView2);
        textView2.setText(sb.toString());
    }
}

