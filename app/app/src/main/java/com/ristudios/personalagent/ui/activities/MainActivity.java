package com.ristudios.personalagent.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.ristudios.personalagent.R;
import com.ristudios.personalagent.utils.Utils;
import com.ristudios.personalagent.utils.notifications.Alarm;
import com.ristudios.personalagent.utils.notifications.NotificationHelper;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        initBurger();
        getSupportActionBar().setTitle("Start");
        NotificationHelper notificationHelper = new NotificationHelper(this);
        notificationHelper.createNotificationChannel(this, "Main Channel", "Handles all notifications", NotificationHelper.MAIN_NOTIFICATION_CHANNEL_ID);
    }


}