package com.oosegroup.fridgefoodtracker.models;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.oosegroup.fridgefoodtracker.R;

import java.util.ArrayList;
import java.util.List;

public class ItemListViewAdapter extends ArrayAdapter {
    private final Activity context;
    private final String[] descriptionArray;
    private final String[] progressBarArray;
    private final Integer[] itemIDArray;
    /*
    private Button expandButton;
    private Button deleteButton;
    private String description;
    private ProgressBar progressBar; */

    public ItemListViewAdapter(Activity context, Fridge fridge) {
        super(context, R.layout.items, fridge.getContent().getItems());

        this.context = context;

        this.descriptionArray = new String[fridge.getContent().getItems().size() * 2];
        this.progressBarArray = new String[fridge.getContent().getItems().size() * 2];
        this.itemIDArray = new Integer[fridge.getContent().getItems().size() * 2];
        for (int i = 0; i < fridge.getContent().getItems().size(); ++i) {
            this.descriptionArray[i] = fridge.getContent().getItems().get(i).getDescription();
            this.progressBarArray[i] = ProgressBar.getView(fridge.getContent().getItems().get(i));
            this.itemIDArray[i] = fridge.getContent().getItems().get(i).getId();
        }

        /*
        List<String> progressBars = new ArrayList<>();
        for (Item item : fridge.getContent().getItems()) {
            progressBars.add(ProgressBar.getView(item));
        }
        this.progressBarArray = (String[]) progressBars.toArray();

        List<Integer> itemIDs = new ArrayList<>();
        for (Item item : fridge.getContent().getItems()) {
            itemIDs.add(item.getId());
        }
        this.itemIDArray = (Integer[]) progressBars.toArray(); */
        /*
        this.expandButton = expandButton;
        this.deleteButton = deleteButton;
        this.description = description;
        this.progressBar = progressBar; */
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.items, null, true);

        // Button expandButton = rowView.findViewById(R.id.expand_btn);
        Button deleteButton = rowView.findViewById(R.id.del_btn);
        TextView description = rowView.findViewById(R.id.list_item_string);
        TextView progressBar = rowView.findViewById(R.id.progress_bar);

        System.out.println(this.itemIDArray.length);

        deleteButton.setTag(this.itemIDArray[position]);
        description.setText(this.descriptionArray[position]);
        progressBar.setText(this.progressBarArray[position]);

        return rowView;
    }
}
