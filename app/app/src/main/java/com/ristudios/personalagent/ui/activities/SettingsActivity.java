package com.ristudios.personalagent.ui.activities;

import android.app.AlarmManager;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.ristudios.personalagent.R;
import com.ristudios.personalagent.ui.fragments.SettingsFragment;
import com.ristudios.personalagent.ui.fragments.TimePickerFragment;
import com.ristudios.personalagent.utils.Utils;
import com.ristudios.personalagent.utils.notifications.Alarm;

/**
 * Responsible for handling the settings of the app by loading the SettingsFragment.
 */
public class SettingsActivity extends BaseActivity implements TimePickerFragment.TimeSetListener, SettingsFragment.TimeModeListener {

    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_settings);
        super.onCreate(savedInstanceState);
        mode = 0;
        initBurgerMenu();
        getSupportActionBar().setTitle(getResources().getString(R.string.settings));
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_view, new SettingsFragment()).commit();
    }

    /**
     * Writes data to shared preferences when the user chooses a time for the notifications.
     * Based on the mode set by {@link #onTimeModeChanged(int)} the method knows whether to overwrite
     * the saved time for evening or morning.
     * @param h Hour to send Notification at.
     * @param m Minute to send Notification at.
     */
    private void writeDataToPrefs(int h, int m) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Alarm alarm = new Alarm();

        if (mode == 1) {
            prefs.edit().putInt(Utils.SP_NOTIFICATION_TIME_ONE_HOUR_KEY, h).putInt(Utils.SP_NOTIFICATION_TIME_ONE_MINUTE_KEY, m).apply();
            long triggerAt = Utils.millisForAlarm(h, m);
            alarm.setRepeatingAlarm(getApplicationContext(), triggerAt, AlarmManager.INTERVAL_DAY, Alarm.REQUEST_CODE_MORNING, Alarm.TYPE_MORNING_ALARM);
            Toast.makeText(this, getResources().getString(R.string.toast_time_set_succes), Toast.LENGTH_SHORT).show();
        } else if (mode == -1) {
            prefs.edit().putInt(Utils.SP_NOTIFICATION_TIME_TWO_HOUR_KEY, h).putInt(Utils.SP_NOTIFICATION_TIME_TWO_MINUTE_KEY, m).apply();
            long triggerAt = Utils.millisForAlarm(h, m);
            alarm.setRepeatingAlarm(getApplicationContext(), triggerAt, AlarmManager.INTERVAL_DAY, Alarm.REQUEST_CODE_EVENING, Alarm.TYPE_EVENING_ALARM);
            Toast.makeText(this, getResources().getString(R.string.toast_time_set_succes), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getResources().getString(R.string.toast_time_set_failure), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Retrieves data from the {@link TimePickerFragment}.
     * @param h Set hour.
     * @param m Set minute.
     */
    @Override
    public void onTimeDataSet(int h, int m) {
        writeDataToPrefs(h, m);
    }

    /**
     * Sets the mode of the activity where 1 is for editing morning reminders while -1 is for evening.
     * @param mode The mode of the activity.
     */
    @Override
    public void onTimeModeChanged(int mode) {
        this.mode = mode;
    }
}