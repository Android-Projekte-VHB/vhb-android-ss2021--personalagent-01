package com.ristudios.personalagent.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.ristudios.personalagent.R;
import com.ristudios.personalagent.utils.Utils;

import org.jetbrains.annotations.NotNull;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SwitchPreference notificationPref, permissionPref;
    private EditTextPreference   weeklyGoalPref, usernamePref;
    private Preference notificationTimeOnePref, notificationTimeTwoPref;
    private TimeModeListener listener;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        listener = (TimeModeListener) context;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
        bindPrefs();
        weeklyGoalPref.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
            @Override
            public void onBindEditText(@NonNull @NotNull EditText editText) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        });

        notificationTimeOnePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showTimePickerDialog(getActivity().findViewById(R.id.notification_one_preference));
                listener.onTimeModeChanged(1);
                return false;
            }
        });

        notificationTimeTwoPref.setOnPreferenceClickListener(p -> {
            showTimePickerDialog(getActivity().findViewById(R.id.notification_two_preference));
            listener.onTimeModeChanged(-1);
            return false;
        });


    }

    public void showTimePickerDialog(View view){
        DialogFragment fragment = new TimePickerFragment();
        fragment.show(getActivity().getSupportFragmentManager(), "myApp:timePicker");
    }

    private void bindPrefs(){
        notificationPref = findPreference(Utils.SP_NOTIFICATION_ENABLED_KEY);
        permissionPref = findPreference(Utils.SP_LOCATION_PERMISSION_KEY);
        notificationTimeOnePref = findPreference(Utils.SP_NOTIFICATION_TIME_ONE_KEY);
        notificationTimeTwoPref = findPreference(Utils.SP_NOTIFICATION_TIME_TWO_KEY);
        weeklyGoalPref = findPreference(Utils.SP_WEEKLY_GOAL_KEY);
        usernamePref = findPreference(Utils.SP_USERNAME_KEY);
    }

    public interface TimeModeListener{
        void onTimeModeChanged(int mode);
    }
}
