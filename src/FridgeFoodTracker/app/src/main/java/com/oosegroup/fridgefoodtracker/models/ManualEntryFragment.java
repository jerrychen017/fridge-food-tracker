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
