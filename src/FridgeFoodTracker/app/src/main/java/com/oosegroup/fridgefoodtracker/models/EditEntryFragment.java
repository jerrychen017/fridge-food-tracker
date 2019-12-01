package com.oosegroup.fridgefoodtracker.models;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.oosegroup.fridgefoodtracker.R;

import java.util.Date;

public class EditEntryFragment extends BottomSheetDialogFragment {

    private int id;
    private String description;
    private Date expDate;

    public EditEntryFragment(int id, String description, Date expDate) {
        this.id = id;
        this.description = description;
        this.expDate = expDate;
    }

    public static EditEntryFragment newInstance(int id, String description, Date expDate) {
        return new EditEntryFragment(id, description, expDate);
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

        EditText descriptionEditText = view.findViewById(R.id.edit_item_text_input);
        descriptionEditText.setText(this.description);

        EditText expDateEditText = view.findViewById(R.id.edit_item_date_input);
        expDateEditText.setText(this.expDate.toString());

        // get the views and attach the listener
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }
}
