package com.oosegroup.fridgefoodtracker.models;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.oosegroup.fridgefoodtracker.Activities.MainActivity;
import com.oosegroup.fridgefoodtracker.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ManualEntryFragment extends BottomSheetDialogFragment {

    private Fridge fridge;
    private MainActivity mainActivity;

    public ManualEntryFragment(Fridge fridge) {
        this.fridge = fridge;
    }

    public static ManualEntryFragment newInstance(Fridge fridge) {
        return new ManualEntryFragment(fridge);
    }

    public void inputItem(View view) {
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

        this.fridge.addItem(item);

        this.buildExpandableListAdapter(this.mainActivity, this.fridge);

        mEdit.setText("");
        dEdit.setText("");
    }

    public void deleteItem(View view) {
        this.fridge.remove(Integer.parseInt(view.getTag().toString()));
        this.buildExpandableListAdapter(this.mainActivity, this.fridge);
    }

    public void buildExpandableListAdapter(Context context, Fridge fridge) {
        Log.d("adapter", "buildExpandableListAdapter: here");
        this.mainActivity.mainItemListView = this.mainActivity.findViewById(R.id.mainItemListView);
        this.mainActivity.detailsMap = this.mainActivity.createDetailsMap(fridge);
        this.mainActivity.expandableListTitle = new ArrayList<String>(this.mainActivity.detailsMap.keySet());
        this.mainActivity.itemListViewAdapter = new ItemListViewAdapter(this.mainActivity, fridge, this.mainActivity.expandableListTitle, this.mainActivity.detailsMap);
        this.mainActivity.mainItemListView.setAdapter(this.mainActivity.itemListViewAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.manual_entry, container,
                false);

        // get the views and attach the listener
        this.mainActivity = (MainActivity) getActivity();
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }
}
