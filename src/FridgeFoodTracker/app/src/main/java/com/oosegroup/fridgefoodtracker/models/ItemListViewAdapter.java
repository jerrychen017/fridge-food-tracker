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

/**
 * the adapter class of the item list
 */
public class ItemListViewAdapter extends BaseExpandableListAdapter {

    // activity of the item list
    private Context context;

    //holds the names of the items
    private final String[] descriptionArray;

    //holds the progress bars of the items
    private final Integer[] progressBarArray;

    //holds the IDs of the items
    private final Integer[] itemIDArray;

    //holds the descriptions
    private List<String> descriptionList;

    //hashes the descriptions
    private HashMap<String, List<String>> detailsMap;

    //the fridge to be converted to a list
    private Fridge fridge;

    /**
     * constructor for item list view
     * @param context the main activity
     * @param fridge the fridge to be converted
     * @param description the name of the item
     * @param details the string of details
     */
    public ItemListViewAdapter(Context context, Fridge fridge, List<String> description,  HashMap<String, List<String>> details) {

        this.context = context;
        this.fridge = fridge;
        this.descriptionList = description;
        this.detailsMap = details;

        this.descriptionArray = new String[fridge.getContent().getItems().size() * 2];
        this.progressBarArray = new Integer[fridge.getContent().getItems().size() * 2];
        this.itemIDArray = new Integer[fridge.getContent().getItems().size() * 2];
        for (int i = 0; i < fridge.getContent().getItems().size(); ++i) {
            this.descriptionArray[i] = fridge.getContent().getItems().get(i).getDescription();
            this.progressBarArray[i] = ProgressBar.getView(fridge.getContent().getItems().get(i));
            this.itemIDArray[i] = fridge.getContent().getItems().get(i).getId();
        }

    }

    /**
     * gets child item from list
     * @param listPosition the position of the item
     * @param expandedListPosition the child position of the item
     * @return
     */
    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.detailsMap.get(this.descriptionList.get(listPosition)).get(expandedListPosition);
    }

    /**
     * returns the id of the child
     * @param listPosition the position of the item
     * @param expandedListPosition the child position of the item
     * @return
     */
    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    /**
     * returns the child view of the item
     * @param listPosition the position of the item
     * @param expandedListPosition the position of the child item
     * @param isLastChild whether it is the last child in the list
     * @param convertView the view
     * @param parent the parent view
     * @return the child view of the item
     */
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

        Button editButton = convertView.findViewById(R.id.items_child_edit_btn);
        editButton.setTag(R.id.TAG_ID, this.itemIDArray[listPosition]);
        editButton.setTag(R.id.TAG_DESCRIPTION, this.descriptionArray[listPosition]);
        editButton.setTag(R.id.TAG_EXP_DATE, this.fridge.getContent().getItems().get(listPosition).getDateExpired());

        Button delButton = convertView.findViewById(R.id.items_child_trash_btn);

        delButton.setTag(this.itemIDArray[listPosition]);

        Button eatButton = convertView.findViewById(R.id.items_child_eat_btn);
        eatButton.setTag(this.itemIDArray[listPosition]);

        // debug
        System.out.println("getChildView: listposistion is " + listPosition);
        System.out.println("getChildView: item id is " + this.itemIDArray[listPosition]);
        System.out.print("ID Array is ");
        for (int i = 0; i < itemIDArray.length; i++) {
            System.out.print(itemIDArray[i] + " ");
        }
        System.out.println();

        LinearLayout buttons = convertView.findViewById(R.id.items_child_buttons);
        if (isLastChild) {
            buttons.setVisibility(View.VISIBLE);
        } else {
            buttons.setVisibility(View.GONE);
        }

        return convertView;
    }

    /**
     * get the amount of children in the list
     * @param listPosition the position of the list
     * @return the size of the list
     */
    @Override
    public int getChildrenCount(int listPosition) {
        return this.detailsMap.get(this.descriptionList.get(listPosition)).size();
    }

    /**
     * gets the group of the list
     * @param listPosition the position of the list
     * @return the size of the list
     */
    @Override
    public Object getGroup(int listPosition) {
        return this.descriptionList.get(listPosition);
    }

    /**
     * gets the count of the group
     * @return the count of the group
     */
    @Override
    public int getGroupCount() {
        return this.descriptionList.size();
    }

    /**
     * gets the id of the group
     * @param listPosition the position of the group
     * @return the id of the group
     */
    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    /**
     * returns the view of the parent in th list
     * @param listPosition the position in the list
     * @param isExpanded whether the list is expanded
     * @param convertView the view
     * @param parent the parent view
     * @return the parent item in the list
     */
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
       android.widget.ProgressBar progressBar = convertView.findViewById(R.id.progressBar);

        listTitleTextView.setText(listTitle);
        progressBar.setProgress(this.progressBarArray[listPosition]);
        return convertView;
    }

    /**
     * whether the ids are set
     * @return whether the ids are set
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * marks items as selectable
     * @param listPosition the item to be picked
     * @param expandedListPosition the position of the child view
     * @return whether the child is selectable
     */
    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
