package com.oosegroup.fridgefoodtracker.models;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.oosegroup.fridgefoodtracker.R;

public class EditEntryFragment extends BottomSheetDialogFragment {

    public static EditEntryFragment newInstance() {
        return new EditEntryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.edit_entry, container,
                false);

        // get the views and attach the listener
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }
}
