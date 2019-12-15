package com.oosegroup.fridgefoodtracker.Activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.oosegroup.fridgefoodtracker.R;
import com.oosegroup.fridgefoodtracker.models.Fridge;
import com.oosegroup.fridgefoodtracker.models.Item;

import java.util.ArrayList;
import java.util.List;

public class RecommendActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    MainActivity mainActivity;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        Fridge fridge = mainActivity.getFridge();
        List<Item> eatenItems = fridge.recommend();
        List<String> eatenString = new ArrayList<>();
        for (Item i: eatenItems) {
            eatenString.add(i.getDescription());
        }
        adapter = new ArrayAdapter<String>(RecommendActivity.this, R.layout.recommend_listview, eatenString);
        ListView listView = (ListView) findViewById(R.id.recommendListView);
        listView.setAdapter(adapter);
    }
}