package com.ristudios.personalagent.ui.fragments;

import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.ristudios.personalagent.R;
import com.ristudios.personalagent.utils.Utils;
import com.ristudios.personalagent.utils.notifications.Alarm;

import org.jetbrains.annotations.NotNull;

public class SettingsFragment extends PreferenceFragmentCompat {

    /**
     * The fragment implemented in the SettingsActivity. It inherits from PreferenceFragmentCompat to easily build functioning Settings.
     */

    private SwitchPreference notificationPref;
    private EditTextPreference dailyGoalPref, usernamePref;
    private Preference notificationTimeOnePref, notificationTimeTwoPref;
    private TimeModeListener listener;
    public static int mode = 0;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        listener = (TimeModeListener) context; //Registers the Activity that uses this Fragment as the TimeModeListener
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
        bindPrefs();
        mode = 0;
        dailyGoalPref.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
            @Override
            public void onBindEditText(@NonNull @NotNull EditText editText) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        });

        notificationTimeOnePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                listener.onTimeModeChanged(1);
                mode = 1;
                showTimePickerDialog(getActivity().findViewById(R.id.notification_one_preference));
                return false;
            }
        });

        notificationTimeTwoPref.setOnPreferenceClickListener(p -> {
            listener.onTimeModeChanged(-1);
            mode = -1;
            showTimePickerDialog(getActivity().findViewById(R.id.notification_two_preference));
            return false;
        });

        notificationPref.setOnPreferenceClickListener(p -> {
            if (notificationPref.isChecked()) {
                Toast.makeText(getActivity(), getResources().getString(R.string.toast_notifications_enabled), Toast.LENGTH_SHORT).show();
                Alarm alarm = new Alarm();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                long triggerAt = Utils.millisForAlarm(prefs.getInt(Utils.SP_NOTIFICATION_TIME_ONE_HOUR_KEY, 7), prefs.getInt(Utils.SP_NOTIFICATION_TIME_ONE_MINUTE_KEY, 0));

                alarm.setRepeatingAlarm(getActivity(), triggerAt, AlarmManager.INTERVAL_DAY, Alarm.REQUEST_CODE_MORNING, Alarm.TYPE_MORNING_ALARM);

                triggerAt = Utils.millisForAlarm(prefs.getInt(Utils.SP_NOTIFICATION_TIME_TWO_HOUR_KEY, 7), prefs.getInt(Utils.SP_NOTIFICATION_TIME_TWO_MINUTE_KEY, 0));
                alarm.setRepeatingAlarm(getActivity(), triggerAt, AlarmManager.INTERVAL_DAY, Alarm.REQUEST_CODE_EVENING, Alarm.TYPE_EVENING_ALARM);
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.toast_notifications_disabled), Toast.LENGTH_SHORT).show();
                Log.d(Utils.LOG_ALARM, "Unchecked");
                Alarm alarm = new Alarm();
                alarm.cancelAlarm(getActivity().getApplicationContext(), Alarm.REQUEST_CODE_MORNING, Alarm.TYPE_MORNING_ALARM);
                alarm.cancelAlarm(getActivity().getApplicationContext(), Alarm.REQUEST_CODE_EVENING, Alarm.TYPE_EVENING_ALARM);
            }
            return false;
        });

    }


    /*
    Shows the TimePicker as an animation for setting the Notification Time.
     */
    public void showTimePickerDialog(View view) {
        DialogFragment fragment = new TimePickerFragment();
        fragment.show(getActivity().getSupportFragmentManager(), "myApp:timePicker");
    }


    /*
    It connects the Settings specified in the settings.xml file with their Java Representation.
     */
    private void bindPrefs() {
        notificationPref = findPreference(Utils.SP_NOTIFICATION_ENABLED_KEY);
        notificationTimeOnePref = findPreference(Utils.SP_NOTIFICATION_TIME_ONE_KEY);
        notificationTimeTwoPref = findPreference(Utils.SP_NOTIFICATION_TIME_TWO_KEY);
        dailyGoalPref = findPreference(Utils.SP_DAILY_GOAL_KEY);
        usernamePref = findPreference(Utils.SP_USERNAME_KEY);
    }

    public interface TimeModeListener {
        void onTimeModeChanged(int mode);
    }
}
