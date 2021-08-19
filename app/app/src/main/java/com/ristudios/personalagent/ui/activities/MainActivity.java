package com.ristudios.personalagent.ui.activities;

import android.app.AlarmManager;
import android.os.Bundle;
import android.util.Log;

import com.ristudios.personalagent.R;
import com.ristudios.personalagent.utils.Utils;
import com.ristudios.personalagent.utils.notifications.Alarm;
import com.ristudios.personalagent.utils.notifications.NotificationHelper;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        initBurgerMenu();
        getSupportActionBar().setTitle(getResources().getString(R.string.start));
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