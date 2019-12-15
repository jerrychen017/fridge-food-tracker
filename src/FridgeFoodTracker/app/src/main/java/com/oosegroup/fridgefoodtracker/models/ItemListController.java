package com.oosegroup.fridgefoodtracker.models;


import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.oosegroup.fridgefoodtracker.Activities.MainActivity;
import com.oosegroup.fridgefoodtracker.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * controller for the item list
 */
public class ItemListController {

    //the fridge holding the list
    static Fridge controllerFridge;

    // main activity holding the fridge
    static MainActivity controllerMainActivity;

    /**
     * Adds item to the fridge
     * @param view the view holding the list
     * @param fridge the fridge for the item to be added to
     * @param mainActivity the main activity
     */
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
            item = new Item(text, date);
        } else {
            item = new Item(text);
        }
        Handler handler = new Handler();

        fridge.addItem(item);
        controllerFridge = fridge;
        controllerMainActivity = mainActivity;
        handler.postDelayed(new Runnable() {
            public void run() {
                callBuildExpandable();
            }
        }, 500);

        mEdit.setText("");
        dEdit.setText("");
    }


    /**
     * inputs item with lifespan
     * @param fridge fridge to be added to
     * @param productName name of the product
     * @param lifespan lifespan of the product
     */
    public static void inputItemWithLifespan(Fridge fridge, String productName, long lifespan){
        Item item = new Item(fridge.getContent().getItems().size(), productName, lifespan);
        fridge.addItem(item);
        controllerFridge = fridge;
    }

    /**
     * deletes item from fridge
     * @param view the view holding the list
     * @param fridge the fridge holding the item
     * @param mainActivity the main activity
     */
    public static void trashItem(View view, Fridge fridge, MainActivity mainActivity) {
        Log.d("ItemListController", "TRASH: " + view.getTag().toString());
        fridge.remove(Integer.parseInt(view.getTag().toString()), false);
        buildExpandableListAdapter(mainActivity, fridge);
    }

    /**
     *
     * @param view the view holding the list
     * @param fridge the fridge holding the item
     * @param mainActivity the main activity
     */
    public static void eatItem(View view, Fridge fridge, MainActivity mainActivity) {
        Log.d("ItemListController", "EAT: " + view.getTag().toString());
        System.out.println("called eatItem with id " + Integer.parseInt(view.getTag().toString())); // debug
        fridge.remove(Integer.parseInt(view.getTag().toString()), true);
        buildExpandableListAdapter(mainActivity, fridge);
    }

    /**
     * edits item in the fridge
     * @param view the view holding the list
     * @param editEnterBtnView the view holding the edit button
     * @param fridge the fridge holding the item
     * @param mainActivity the main activity
     */
    public static void editItem(View view, View editEnterBtnView, Fridge fridge, MainActivity mainActivity) {
        Log.d("ItemListController", "EDIT: " + editEnterBtnView.getTag(R.id.TAG_ID).toString());
        EditText mEdit = (EditText) view.findViewById(R.id.edit_item_text_input);
        EditText dEdit = (EditText) view.findViewById(R.id.edit_item_date_input);

        String date_s = dEdit.getText().toString();
        Date date;

        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(date_s);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            date = new Date(new Date().getTime() + 86400000);
            Log.i("invalid date", e.toString());
        }

        String description = mEdit.getText().toString();
        if (description.equals("")) {
            description = "DEFAULT_NAME";
        }
        fridge.edit(Integer.parseInt(editEnterBtnView.getTag(R.id.TAG_ID).toString()),
                description, date);

        buildExpandableListAdapter(mainActivity, fridge);
    }

    /**
     * builds the adapter to populate the list
     * @param mainActivity the main activity
     * @param fridge the fridge with items
     */
    public static void buildExpandableListAdapter(MainActivity mainActivity, Fridge fridge) {
        Log.d("adapter", "buildExpandableListAdapter: here");
        sort(mainActivity, fridge);
        mainActivity.mainItemListView = mainActivity.findViewById(R.id.mainItemListView);
        mainActivity.detailsMap = mainActivity.createDetailsMap(fridge);
        mainActivity.expandableListTitle = new ArrayList<String>(mainActivity.detailsMap.keySet());
        mainActivity.itemListViewAdapter = new ItemListViewAdapter(mainActivity, fridge, mainActivity.expandableListTitle, mainActivity.detailsMap);
        mainActivity.mainItemListView.setAdapter(mainActivity.itemListViewAdapter);
    }

    /**
     * sorts the items via various methods
     * @param mainActivity
     * @param fridge
     */
    private static void sort(MainActivity mainActivity, Fridge fridge) {
        String sortingState = mainActivity.sortingState;

        if (sortingState.equals("NONE")) {
            return;
        } else if (sortingState.equals("EXPIRATION")) {
            fridge.sortByExpiration();
        } else if (sortingState.equals("ENTER")) {
            fridge.sortByEntryDate();
        }
    }

    /**
     * builds the adapter
     */
    public static void callBuildExpandable() {
        buildExpandableListAdapter(controllerMainActivity, controllerFridge);
    }

    /**
     * links the controller to a fridge
     * @param fridge fridge to be linked
     */
    public static void setControllerFridge(Fridge fridge){
        controllerFridge = fridge;
    }

    /**
     * sets the main activity of the controller
     * @param mainActivity the main activity
     */
    public static void setControllerMainActivity(MainActivity mainActivity){
        controllerMainActivity = mainActivity;
    }

    /**
     * returns the main activity of the controller
     * @return the main activity of the controller
     */
    public static MainActivity getControllerMainActivity(){
        return controllerMainActivity;
    }

    /**
     * returns the fridge of the controller
     * @return the fridge of the controller
     */
    public static Fridge getControllerFridge(){
        return controllerFridge;
    }
}

