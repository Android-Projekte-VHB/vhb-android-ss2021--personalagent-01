package com.ristudios.personalagent.ui.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.time.LocalTime;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        LocalTime current = LocalTime.now();
        return new TimePickerDialog(getActivity(), this, current.getHour(), current.getMinute(), true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        //TODO: Save changes in Prefs, adjust alarms.
        Log.d("TIMEPICKER_KEY", "Time set to " + hourOfDay +":"+ minute);
        
    }
}
