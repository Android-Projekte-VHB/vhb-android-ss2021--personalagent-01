package com.ristudios.personalagent.ui.activities;

import android.app.AlarmManager;
import android.os.Bundle;
import android.util.Log;

import com.ristudios.personalagent.R;
import com.ristudios.personalagent.utils.Utils;
import com.ristudios.personalagent.utils.notifications.Alarm;
import com.ristudios.personalagent.utils.notifications.NotificationHelper;



/**
 * LauncherActivity, shows tasks for current day as well as weather information.
 */

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        initBurgerMenu();
        getSupportActionBar().setTitle(getResources().getString(R.string.start));
        setupNotificationChannel();
    }

    /**
     *
     */
    private void setupNotificationChannel() {
        NotificationHelper notificationHelper = new NotificationHelper(this);
        notificationHelper.createNotificationChannel(this,
                getResources().getString(R.string.notification_channel_name),
                getResources().getString(R.string.notification_channel_description),
                NotificationHelper.MAIN_NOTIFICATION_CHANNEL_ID);
        initializeAlarms();
    }

    private void initializeAlarms() {
        //TODO: Check if user has disabled notifications for morning/evening
        //TODO: Check at what time the user wants to receive notifications
        Alarm alarm = new Alarm();
        long triggerAt = Utils.millisForAlarm(7, 0);
        alarm.setRepeatingAlarm(this, triggerAt, AlarmManager.INTERVAL_DAY, Alarm.REQUEST_CODE_MORNING, Alarm.TYPE_MORNING_ALARM);
        triggerAt = Utils.millisForAlarm(19, 0);
        alarm.setRepeatingAlarm(this, triggerAt, AlarmManager.INTERVAL_DAY, Alarm.REQUEST_CODE_EVENING, Alarm.TYPE_EVENING_ALARM);

    }


}