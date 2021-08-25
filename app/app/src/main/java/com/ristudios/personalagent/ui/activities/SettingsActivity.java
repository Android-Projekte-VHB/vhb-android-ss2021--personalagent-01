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
 * Responsible for handling the settings of the app.
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

    private void writeDataToPrefs(int h, int m)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Alarm alarm = new Alarm();

        if (mode == 1){ //HINT: Time for morning notifications
            prefs.edit().putInt(Utils.SP_NOTIFICATION_TIME_ONE_HOUR_KEY, h).putInt(Utils.SP_NOTIFICATION_TIME_ONE_MINUTE_KEY, m).apply();
            long triggerAt = Utils.millisForAlarm(h, m);
            //alarm.cancelAlarm(getApplicationContext(), Alarm.REQUEST_CODE_MORNING, Alarm.TYPE_MORNING_ALARM);
            alarm.setRepeatingAlarm(getApplicationContext(), triggerAt, AlarmManager.INTERVAL_DAY, Alarm.REQUEST_CODE_MORNING, Alarm.TYPE_MORNING_ALARM);
            Toast.makeText(this, getResources().getString(R.string.toast_time_set_succes), Toast.LENGTH_SHORT).show();
        }
        else if (mode == -1){ //HINT: Time for evening notifications
            prefs.edit().putInt(Utils.SP_NOTIFICATION_TIME_TWO_HOUR_KEY, h).putInt(Utils.SP_NOTIFICATION_TIME_TWO_MINUTE_KEY, m).apply();
            long triggerAt = Utils.millisForAlarm(h, m);
            //alarm.cancelAlarm(getApplicationContext(), Alarm.REQUEST_CODE_EVENING, Alarm.TYPE_EVENING_ALARM);
            alarm.setRepeatingAlarm(getApplicationContext(), triggerAt, AlarmManager.INTERVAL_DAY, Alarm.REQUEST_CODE_EVENING, Alarm.TYPE_EVENING_ALARM);
            Toast.makeText(this, getResources().getString(R.string.toast_time_set_succes), Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, getResources().getString(R.string.toast_time_set_failure), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTimeDataSet(int h, int m) {
        writeDataToPrefs(h, m);
    }

    @Override
    public void onTimeModeChanged(int mode) {
        this.mode = mode;
    }
}