package com.ristudios.personalagent.ui.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
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
import com.ristudios.personalagent.data.Entry;
import com.ristudios.personalagent.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;
import java.util.Objects;


public class AddOrUpdateEntryDialogFragment extends DialogFragment {

    public static final int MODE_NEW = 1;
    public static final int MODE_UPDATE = -1;

    private AddEntryDialogClickListener listener;
    private ZonedDateTime targetDateTime;
    private EditText edtName, edtHours, edtMinutes;
    private Spinner spnCategory, spnDifficulty;
    private TextView txtTimeSeparator, txtTimeHeader;
    private int mode; //1 for new entry, -1 for editing an existing one+
    private Entry entry;
    private int position;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        listener = (AddEntryDialogClickListener) context;
    }

    public void setTargetDateTime(ZonedDateTime targetDateTime) {
        this.targetDateTime = targetDateTime;
    }

    /**
     * Sets the mode of the dialog to either create a new entry or update an old one.
     *
     * @param mode The mode to use.
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

    /**
     * Sets the entry of the dialog if it is in update mode so the data of the existing entry can be displayed.
     *
     * @param entry    The entry to edit.
     * @param position The position the entry has in the RecyclerView.
     */
    public void setEntry(Entry entry, int position) {
        this.entry = entry;
        this.position = position;
    }

    /**
     * Fills out the dialog with data if an entry is set. If not all fields will remain default.
     */
    public void setData() {
        if (entry != null) {
            edtName.setText(entry.getName());
            switch (entry.getCategory()) {
                case WORK:
                    spnCategory.setSelection(0);
                    break;
                case HOBBY:
                    spnCategory.setSelection(1);
                    break;
                case FITNESS:
                    spnCategory.setSelection(2);
                    break;
                case APPOINTMENT:
                    spnCategory.setSelection(3);
                    break;
            }
            switch (entry.getDifficulty()) {
                case EASY:
                    spnDifficulty.setSelection(0);
                    break;
                case MEDIUM:
                    spnDifficulty.setSelection(1);
                    break;
                case HARD:
                    spnDifficulty.setSelection(2);
                    break;
                case NONE:
                    spnDifficulty.setSelection(3);
                    spnDifficulty.setEnabled(false);
            }
            targetDateTime = Utils.getDateFromMillis(entry.getDate());
            if (entry.getCategory().equals(Category.APPOINTMENT)) {
                edtHours.setText(String.valueOf(targetDateTime.getHour()));
                edtMinutes.setText(String.valueOf(targetDateTime.getMinute()));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initViews();
        setData();
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
                        AddOrUpdateEntryDialogFragment.this.getDialog().dismiss();
                        listener.onNegativeClicked(mode);
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
                        if (nameEntered()) {
                            if (timeSet()) {
                                if (timeValuesValid()) {
                                    if (mode == 1) {
                                        listener.onItemNew(getResultName(), getResultTimeHours(), getResultTimeMinutes(), getResultCategory(), getResultDifficulty(), targetDateTime);
                                    } else if (mode == -1) {
                                        listener.onItemUpdate(getResultName(), getResultTimeHours(), getResultTimeMinutes(), getResultCategory(), getResultDifficulty(), entry, position, targetDateTime);
                                    }
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_entry_invalid_time), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), getResources().getString(R.string.toast_entry_invalid_time), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.toast_entry_invalid_name), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return dialog;
    }


    //region information collectors

    /**
     * Checks if the time has been set. If the entry is not an appointment and no time is set the time will automatically be set to 12:00
     */
    private void checkForTime() {
        if (spnCategory.getSelectedItemPosition() != 3) {
            if (!timeSet()) {
                edtHours.setText("12");
                edtMinutes.setText("00");
            }
        }
    }


    /**
     * Gets the name of edtName.
     *
     * @return Name.
     */
    private String getResultName() {
        return edtName.getText().toString();
    }

    /**
     * Gets the hours of edtHours.
     *
     * @return Hours.
     */
    private int getResultTimeHours() {
        Log.d(Utils.LOG_ALARM, edtHours.getText().toString());
        return Integer.parseInt(edtHours.getText().toString());

    }

    private int getResultTimeMinutes() {
        Log.d(Utils.LOG_ALARM, edtMinutes.getText().toString());
        return Integer.parseInt(edtMinutes.getText().toString());
    }

    /**
     * Gets the current selection of the categorySpinner.
     *
     * @return Category.
     */
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
            case 3:
                selected = Difficulty.NONE;
                break;
        }
        return selected;
    }

    //endregion

    //region validity checks

    /**
     * Checks if all the EditTexts contain a value. Only if all EditTexts aren't empty the app will proceed.
     *
     * @return False if any of the TextViews is empty, else true.
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
                    spnDifficulty.setSelection(3);
                    spnDifficulty.setEnabled(false);
                } else {

                    edtHours.setVisibility(View.GONE);
                    edtMinutes.setVisibility(View.GONE);
                    txtTimeSeparator.setVisibility(View.GONE);
                    txtTimeHeader.setVisibility(View.GONE);
                    spnDifficulty.setSelection(0);
                    spnDifficulty.setEnabled(true);

                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnDifficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 3 && spnCategory.getSelectedItemPosition() != 3) {
                    spnDifficulty.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //endregion

    public interface AddEntryDialogClickListener {
        void onItemNew(String name, int hour, int minute, Category category, Difficulty difficulty, ZonedDateTime targetDate);

        void onItemUpdate(String name, int hour, int minute, Category category, Difficulty difficulty, Entry oldEntry, int position, ZonedDateTime targetDate);

        void onNegativeClicked(int mode);
    }
}
