package com.oosegroup.fridgefoodtracker.models;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.oosegroup.fridgefoodtracker.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemListViewAdapter extends BaseExpandableListAdapter {
    private Context context;
    //private final String[] descriptionArray;
    //private final String[] progressBarArray;
    //private final Integer[] itemIDArray;
    private List<String> descriptionList;
    private List<String> progressBarList;
    private List<Integer> itemIDList;
    private HashMap<String, List<String>> detailsMap;
    /*
    private Button expandButton;
    private Button deleteButton;
    private String description;
    private ProgressBar progressBar; */

    public ItemListViewAdapter(Context context, List<String> description, List<String> progressBar, List<Integer> itemID, HashMap<String, List<String>> details) {

        this.context = context;
        this.descriptionList = description;
        this.progressBarList = progressBar;
        this.itemIDList = itemID;
        this.detailsMap = details;

        /*this.descriptionArray = new String[fridge.getContent().getItems().size() * 2];
        this.progressBarArray = new String[fridge.getContent().getItems().size() * 2];
        this.itemIDArray = new Integer[fridge.getContent().getItems().size() * 2];
        for (int i = 0; i < fridge.getContent().getItems().size(); ++i) {
            this.descriptionArray[i] = fridge.getContent().getItems().get(i).getDescription();
            this.progressBarArray[i] = ProgressBar.getView(fridge.getContent().getItems().get(i));
            this.itemIDArray[i] = fridge.getContent().getItems().get(i).getId();
        }*/


    }
    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.detailsMap.get(this.descriptionList.get(listPosition)).get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.items_child, null);
        }
        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.inpDate);
        expandedListTextView.setText(expandedListText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.detailsMap.get(this.descriptionList.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.descriptionList.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.descriptionList.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.items, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.list_item_string);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

   /* @Override
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
    }*/
}
