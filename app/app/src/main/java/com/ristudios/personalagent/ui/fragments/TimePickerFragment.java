package com.ristudios.personalagent.ui.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import com.ristudios.personalagent.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    TimeSetListener listener;

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        LocalTime current = LocalTime.now();
        int h = current.getHour();
        int m = current.getMinute();
        int mode = SettingsFragment.mode;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        if (prefs.contains(Utils.SP_NOTIFICATION_TIME_ONE_HOUR_KEY) && mode == 1) {
            h = prefs.getInt(Utils.SP_NOTIFICATION_TIME_ONE_HOUR_KEY, 7);
            m = prefs.getInt(Utils.SP_NOTIFICATION_TIME_ONE_MINUTE_KEY, 0);
        }
        if (prefs.contains(Utils.SP_NOTIFICATION_TIME_TWO_HOUR_KEY) && mode == -1) {
            h = prefs.getInt(Utils.SP_NOTIFICATION_TIME_TWO_HOUR_KEY, 19);
            m = prefs.getInt(Utils.SP_NOTIFICATION_TIME_TWO_MINUTE_KEY, 0);
        }

        return new TimePickerDialog(getActivity(), this, h, m, true);
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        listener = (TimeSetListener) context;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        listener.onTimeDataSet(hourOfDay, minute);
    }

    public interface TimeSetListener {
        void onTimeDataSet(int h, int m);
    }
}
