package com.ristudios.personalagent.ui.activities;

import android.os.Bundle;

import com.ristudios.personalagent.R;
import com.ristudios.personalagent.utils.notifications.NotificationHelper;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        initBurgerMenu();
        getSupportActionBar().setTitle("Start");
        NotificationHelper notificationHelper = new NotificationHelper(this);
        notificationHelper.createNotificationChannel(this, "Main Channel", "Handles all notifications", NotificationHelper.MAIN_NOTIFICATION_CHANNEL_ID);
    }


}