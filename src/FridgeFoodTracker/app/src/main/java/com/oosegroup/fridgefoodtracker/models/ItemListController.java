package com.oosegroup.fridgefoodtracker.models;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.oosegroup.fridgefoodtracker.Activities.MainActivity;
import com.oosegroup.fridgefoodtracker.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ItemListController {

    public static void inputItem(View view, Fridge fridge, MainActivity mainActivity) {
        EditText mEdit = (EditText) view.findViewById(R.id.item_text_input);
        EditText dEdit = (EditText) view.findViewById(R.id.item_date_input);
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

        fridge.addItem(item);

        buildExpandableListAdapter(mainActivity, fridge);

        mEdit.setText("");
        dEdit.setText("");
    }

    public static void deleteItem(View view, Fridge fridge, MainActivity mainActivity) {
        fridge.remove(Integer.parseInt(view.getTag().toString()));
        buildExpandableListAdapter(mainActivity, fridge);
    }

    public static void buildExpandableListAdapter(MainActivity mainActivity, Fridge fridge) {
        Log.d("adapter", "buildExpandableListAdapter: here");
        mainActivity.mainItemListView = mainActivity.findViewById(R.id.mainItemListView);
        mainActivity.detailsMap = mainActivity.createDetailsMap(fridge);
        mainActivity.expandableListTitle = new ArrayList<String>(mainActivity.detailsMap.keySet());
        mainActivity.itemListViewAdapter = new ItemListViewAdapter(mainActivity, fridge, mainActivity.expandableListTitle, mainActivity.detailsMap);
        mainActivity.mainItemListView.setAdapter(mainActivity.itemListViewAdapter);
    }
}
