package com.oosegroup.fridgefoodtracker.models;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oosegroup.fridgefoodtracker.R;

import java.util.HashMap;
import java.util.List;

public class ItemListViewAdapter extends BaseExpandableListAdapter {
    private Context context;
    private final String[] descriptionArray;
    private final String[] progressBarArray;
    private final Integer[] itemIDArray;
    private List<String> descriptionList;
    private HashMap<String, List<String>> detailsMap;

    public ItemListViewAdapter(Context context, Fridge fridge, List<String> description,  HashMap<String, List<String>> details) {

        this.context = context;
        this.descriptionList = description;
        this.detailsMap = details;

        this.descriptionArray = new String[fridge.getContent().getItems().size() * 2];
        this.progressBarArray = new String[fridge.getContent().getItems().size() * 2];
        this.itemIDArray = new Integer[fridge.getContent().getItems().size() * 2];
        for (int i = 0; i < fridge.getContent().getItems().size(); ++i) {
            this.descriptionArray[i] = fridge.getContent().getItems().get(i).getDescription();
            this.progressBarArray[i] = ProgressBar.getView(fridge.getContent().getItems().get(i));
            this.itemIDArray[i] = fridge.getContent().getItems().get(i).getId();
        }

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
        Log.d("child view", "getChildView: ");
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.items_child, null);
        }

        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.items_child);
        expandedListTextView.setText(expandedListText);

        LinearLayout buttons = convertView.findViewById(R.id.items_child_buttons);
        if (isLastChild) {
            buttons.setVisibility(View.VISIBLE);

            Button editButton = convertView.findViewById(R.id.items_child_edit_btn);
            editButton.setTag(this.itemIDArray[listPosition]);

            Button delButton = convertView.findViewById(R.id.items_child_delete_btn);
            delButton.setTag(this.itemIDArray[listPosition]);
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.detailsMap.get(this.descriptionList.get(listPosition)).size();
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
        Button delButton = convertView.findViewById(R.id.del_btn);
        TextView progressBar = convertView.findViewById(R.id.progress_bar);

        listTitleTextView.setText(listTitle);
        delButton.setTag(this.itemIDArray[listPosition]);
        progressBar.setText(this.progressBarArray[listPosition]);
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
}
