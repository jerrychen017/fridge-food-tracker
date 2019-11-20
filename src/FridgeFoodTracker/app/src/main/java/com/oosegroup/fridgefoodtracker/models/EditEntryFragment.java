package com.oosegroup.fridgefoodtracker.models;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.oosegroup.fridgefoodtracker.R;

public class EditEntryFragment extends BottomSheetDialogFragment {

    private int id;

    public EditEntryFragment(int id) {
        this.id = id;
    }

    public static EditEntryFragment newInstance(int id) {
        return new EditEntryFragment(id);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.edit_entry, container,
                false);

        Button enterButton = view.findViewById(R.id.edit_enter_btn);
        enterButton.setTag(this.id);

        // get the views and attach the listener
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }
}
