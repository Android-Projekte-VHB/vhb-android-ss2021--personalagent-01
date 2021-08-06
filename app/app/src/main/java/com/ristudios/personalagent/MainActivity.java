package com.ristudios.personalagent;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.os.Bundle;

import com.ristudios.personalagent.utils.NotificationHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationHelper notificationHelper = new NotificationHelper(this);
        notificationHelper.createNotificationChannel(this, "Main Channel", "Handles all notifications", NotificationHelper.MAIN_NOTIFICATION_CHANNEL_ID);
        setContentView(R.layout.activity_main);
    }
}