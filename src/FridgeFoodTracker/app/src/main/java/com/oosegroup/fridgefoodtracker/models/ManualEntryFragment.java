package com.oosegroup.fridgefoodtracker.models;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;

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
    public static final int REQUEST_CODE = 11; // Used to identify the result
    String selectedDate;
    EditText dateEntry;

    public ManualEntryFragment(Fridge fridge) {
        this.fridge = fridge;
    }

    public static ManualEntryFragment newInstance(Fridge fridge) {
        return new ManualEntryFragment(fridge);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

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

        // get the views and attach the listener
        this.mainActivity = (MainActivity) getActivity();
        return view;

    }

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



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }
}
