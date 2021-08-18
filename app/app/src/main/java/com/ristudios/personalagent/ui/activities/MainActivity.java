package com.ristudios.personalagent.ui.activities;

import android.os.Bundle;

import com.ristudios.personalagent.R;
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
    }


}