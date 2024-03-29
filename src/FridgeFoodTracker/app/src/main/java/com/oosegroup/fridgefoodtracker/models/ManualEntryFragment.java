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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.oosegroup.fridgefoodtracker.Activities.CameraActivity;
import com.oosegroup.fridgefoodtracker.R;

/**
 * manual entry fragment for adding an item
 */
public class ManualEntryFragment extends BottomSheetDialogFragment {

    // id for identifying result
    public static final int REQUEST_CODE = 11; // Used to identify the result

    //expiration date for the item
    String selectedDate;

    //edittext for entering exp. date
    EditText dateEntry;

    /**
     * constructor for the manual entry fragment
     */
    public ManualEntryFragment() { }

    /**
     * creates view of the fragment
     * @return a new instance of the entry fragment
     */
    public static ManualEntryFragment newInstance() {
        return new ManualEntryFragment();
    }

    /**
     * creates the view
     * @param inflater inflater for inflating view
     * @param container container holding the fragment
     * @param savedInstanceState previous state of the parent activity
     * @return the entry view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);

        View view = inflater.inflate(R.layout.manual_entry, container,
                false);

        final FragmentManager fm = ((AppCompatActivity)getActivity()).getSupportFragmentManager();
        dateEntry = view.findViewById(R.id.item_date_input);
        dateEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create the datePickerFragment
                AppCompatDialogFragment newFragment = new DatePickerFragment();
                // set the targetFragment to receive the results, specifying the request code
                newFragment.setTargetFragment(ManualEntryFragment.this, REQUEST_CODE);
                // show the datePicker
                newFragment.show(fm, "datePicker");
            }
        });



        Button start_camera_button = (Button) view.findViewById(R.id.start_camera_button);
        // Capture button clicks
        start_camera_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(getActivity(),
                        CameraActivity.class);
                startActivity(myIntent);
            }
        });

        // get the views and attach the listener
        return view;
    }

    /**
     * returns the result of the activity
     * @param requestCode for identifying the result
     * @param resultCode ok's the result
     * @param data the data to be returned
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
     * creates view
     * @param view to be created
     * @param savedInstanceState previous state
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }
}
