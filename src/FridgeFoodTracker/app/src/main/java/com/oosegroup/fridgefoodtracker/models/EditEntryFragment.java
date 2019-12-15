package com.oosegroup.fridgefoodtracker.models;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.oosegroup.fridgefoodtracker.R;

import java.util.Date;

/**
 * Fragment that allows user to edit an item in the fridge
 */
public class EditEntryFragment extends BottomSheetDialogFragment {

    // id of the item
    private int id;

    // name of the item
    private String description;

    // expiration date of the item
    private Date expDate;

    // used to trigger the datepicker fragment
    private static final int REQUEST_CODE = 11;

    // selected date of the item
    private String selectedDate;

    // edittext for entering date
    private EditText dateEntry;

    /**
     * A constructor that creates the EditEntry Fragment
     * @param id the id of the item
     * @param description the name of the item
     * @param expDate the expiration date of the item
     */
    public EditEntryFragment(int id, String description, Date expDate) {
        this.id = id;
        this.description = description;
        this.expDate = expDate;
    }

    /**
     * Returns a new instance of the EditEntry Fragment
     * @param id the id of the item
     * @param description the name of the item
     * @param expDate the expiration date of the item
     * @return a new instance of the EditEntry Fragment
     */
    public static EditEntryFragment newInstance(int id, String description, Date expDate) {
        return new EditEntryFragment(id, description, expDate);
    }

    /**
     *
     * @param inflater inflater for inflating the layout
     * @param container container that holds the view
     * @param savedInstanceState previously saved state of the parent activity
     * @return the fragment view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.edit_entry, container,
                false);

        final FragmentManager fm = ((AppCompatActivity)getActivity()).getSupportFragmentManager();
        this.dateEntry = view.findViewById(R.id.edit_item_date_input);
        this.dateEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create the datePickerFragment
                AppCompatDialogFragment newFragment = new DatePickerFragment();
                // set the targetFragment to receive the results, specifying the request code
                newFragment.setTargetFragment(EditEntryFragment.this, REQUEST_CODE);
                // show the datePicker
                newFragment.show(fm, "datePicker");
            }
        });

        Button enterButton = view.findViewById(R.id.edit_enter_btn);
        enterButton.setTag(R.id.TAG_ID, this.id);

        EditText descriptionEditText = view.findViewById(R.id.edit_item_text_input);
        descriptionEditText.setText(this.description);

        this.dateEntry.setText(this.expDate.toString());

        // get the views and attach the listener
        return view;
    }

    /**
     * Returns the result of the fragment
     * @param requestCode for identifying the result
     * @param resultCode for ok'ing the result
     * @param data the data returned
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check for the results
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // get date from string
            selectedDate = data.getStringExtra("selectedDate");
            // set the value of the editText
            dateEntry.setText(selectedDate);
        }
    }

    /**
     * create view
     * @param view view used
     * @param savedInstanceState previous saved state of activity
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }
}
