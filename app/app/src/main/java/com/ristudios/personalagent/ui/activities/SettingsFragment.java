package com.ristudios.personalagent.ui.activities;

import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.ristudios.personalagent.R;
import com.ristudios.personalagent.utils.Utils;

import org.jetbrains.annotations.NotNull;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SwitchPreference notificationPref, permissionPref;
    private EditTextPreference notificationTimeOnePref, notificationTimeTwoPref, weeklyGoalPref, usernamePref;

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
    }

    private void bindPrefs(){
        notificationPref = findPreference(Utils.SP_NOTIFICATION_ENABLED_KEY);
        permissionPref = findPreference(Utils.SP_LOCATION_PERMISSION_KEY);
        notificationTimeOnePref = findPreference(Utils.SP_NOTIFICATION_TIME_ONE_KEY);
        notificationTimeTwoPref = findPreference(Utils.SP_NOTIFICATION_TIME_TWO_KEY);
        weeklyGoalPref = findPreference(Utils.SP_WEEKLY_GOAL_KEY);
        usernamePref = findPreference(Utils.SP_USERNAME_KEY);
    }
}
