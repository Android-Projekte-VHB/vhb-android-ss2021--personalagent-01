package com.ristudios.personalagent.ui.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ristudios.personalagent.R;
import com.ristudios.personalagent.data.Category;
import com.ristudios.personalagent.data.Difficulty;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class AddEntryDialogFragment extends DialogFragment {

    private AddEntryDialogClickListener listener;
    private EditText edtName, edtHours, edtMinutes;
    private Spinner spnCategory, spnDifficulty;
    private TextView txtTimeSeparator, txtTimeHeader;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        listener = (AddEntryDialogClickListener) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        initViews();
        adjustLayout();

    }


    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        AlertDialog dialog = builder.setView(inflater.inflate(R.layout.dialog_add_entry, null))
                .setPositiveButton(getResources().getString(R.string.dialog_yes_option), null) //listener is null since button onClick will be overwritten later on.
                .setNegativeButton(getResources().getString(R.string.dialog_no_option), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddEntryDialogFragment.this.getDialog().cancel();
                        listener.onNegativeClicked();
                    }
                }).create();

        //setting custom onShowListener so the dialog is only dismissed when the operation was a success.
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkForTime();
                        if (nameEntered())
                        {
                            if (timeSet())
                            {
                                if (timeValuesValid())
                                {
                                    listener.onPositiveClicked(getResultName(), getResultTimeHours(), getResultTimeMinutes(), getResultCategory(), getResultDifficulty());
                                    dialog.dismiss();
                                }
                                else {
                                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_entry_invalid_time), Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(getActivity(), getResources().getString(R.string.toast_entry_invalid_time), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.toast_entry_invalid_name), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return dialog;
    }

    private void checkForTime() {
        if (spnCategory.getSelectedItemPosition() != 3) {
            if (!timeSet()){
                edtHours.setText("12");
                edtMinutes.setText("00");
            }
        }
    }


    //region information collectors


    private String getResultName() {
        return edtName.getText().toString();
    }

    private int getResultTimeHours() {
        return Integer.parseInt(edtHours.getText().toString());
    }

    private int getResultTimeMinutes() {
        return Integer.parseInt(edtMinutes.getText().toString());
    }

    private Category getResultCategory() {
        Category selected = Category.WORK;
        switch (spnCategory.getSelectedItemPosition()) {
            case 0:
                selected = Category.WORK;
                break;
            case 1:
                selected = Category.HOBBY;
                break;
            case 2:
                selected = Category.FITNESS;
                break;
            case 3:
                selected = Category.APPOINTMENT;
                break;
        }
        return selected;
    }

    private Difficulty getResultDifficulty() {
        Difficulty selected = Difficulty.EASY;
        switch (spnDifficulty.getSelectedItemPosition()) {
            case 0:
                selected = Difficulty.EASY;
                break;
            case 1:
                selected = Difficulty.MEDIUM;
                break;
            case 2:
                selected = Difficulty.HARD;
                break;
        }
        return selected;
    }

    //endregion

    //region validity checks

    /**
     * Checks if all the EditTexts contain a value. Only if all EditTexts aren't empty the app will proceed.
     *
     * @return False if anyone of the TextViews is empty, else true.
     */
    private boolean nameEntered() {
        return !edtName.getText().toString().isEmpty();
    }

    /**
     * Checks if the user selected a valid time (hour < 24, minute <60).
     *
     * @return False if hour > 24 or minute > 59, else true.
     */
    private boolean timeValuesValid() {
        if (getResultTimeMinutes() > 59 || getResultTimeHours() > 23) {
            return false;
        }
        return true;
    }

    private boolean timeSet() {
        if (edtHours.getText().toString().isEmpty() || edtMinutes.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }

    //endregion

    //region views

    /**
     * Initializes the views of the dialog
     */
    @SuppressLint("SetTextI18n")
    private void initViews() {
        edtHours = Objects.requireNonNull(getDialog()).findViewById(R.id.edt_hours);
        edtMinutes = getDialog().findViewById(R.id.edt_minutes);
        edtName = getDialog().findViewById(R.id.edt_entry_name);
        spnCategory = getDialog().findViewById(R.id.spn_category);
        spnDifficulty = getDialog().findViewById(R.id.spn_difficulty);
        txtTimeSeparator = getDialog().findViewById(R.id.txt_time_separator);
        txtTimeHeader = getDialog().findViewById(R.id.txt_time_header);
    }

    /**
     * Adjusts the layout of the dialog. When Appointment is selected as Category elements will appear
     * to let the user enter a time. In any other case said layout elements will disappear as the exact
     * point of time is not relevant for other categories.
     */
    private void adjustLayout() {
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 3) {
                    edtHours.setVisibility(View.VISIBLE);
                    edtMinutes.setVisibility(View.VISIBLE);
                    txtTimeSeparator.setVisibility(View.VISIBLE);
                    txtTimeHeader.setVisibility(View.VISIBLE);
                } else {

                    edtHours.setVisibility(View.GONE);
                    edtMinutes.setVisibility(View.GONE);
                    txtTimeSeparator.setVisibility(View.GONE);
                    txtTimeHeader.setVisibility(View.GONE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //endregion

    public interface AddEntryDialogClickListener {
        void onPositiveClicked(String name, int hour, int minute, Category category, Difficulty difficulty);

        void onNegativeClicked();
    }
}
