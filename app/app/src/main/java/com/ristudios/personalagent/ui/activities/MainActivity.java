package com.ristudios.personalagent.ui.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.ristudios.personalagent.R;
import com.ristudios.personalagent.data.api.Weather;
import com.ristudios.personalagent.data.api.WeatherDataListener;
import com.ristudios.personalagent.data.api.WeatherDataProvider;
import com.ristudios.personalagent.utils.Utils;
import com.ristudios.personalagent.utils.notifications.Alarm;
import com.ristudios.personalagent.utils.notifications.NotificationHelper;



/**
 * LauncherActivity, shows tasks for current day as well as weather information.
 */

public class MainActivity extends BaseActivity implements WeatherDataListener {

    //For API-testing
    private WeatherDataProvider provider;
    private static final int REQUEST_LOCATION_PERMISSIONS = 10;
    private TextView tempTV, tempMaxTV, tempMinTV, precipitationTV;
    private ImageView weatherIcon;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        initBurgerMenu();
        initUI();
        initAPI();
        getSupportActionBar().setTitle(getResources().getString(R.string.start));
        setupNotificationChannel();
        initializeAlarms();
    }

    /**
     *
     */


    //TODO: SUBJECT TO CHANGE! only for testing purposes - nothing final
    private void initUI(){
        tempTV = findViewById(R.id.tv);
        weatherIcon = findViewById(R.id.imageEEEE);
        tempMaxTV = findViewById(R.id.maxTV);
        tempMinTV = findViewById(R.id.minTV);
        precipitationTV = findViewById(R.id.precipitationTV);
    }

    private void initAPI(){
        requestLocationPermissions();
        provider = new WeatherDataProvider(getApplicationContext(), this);
        provider.update();
    }


    //TODO: Request Permissions outside of OnCreate() or check if they were denied
    private void requestLocationPermissions() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        requestPermissions(permissions, REQUEST_LOCATION_PERMISSIONS);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    break;
                }
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void setupNotificationChannel() {
        NotificationHelper notificationHelper = new NotificationHelper(this);
        notificationHelper.createNotificationChannel(this,
                getResources().getString(R.string.notification_channel_name),
                getResources().getString(R.string.notification_channel_description),
                NotificationHelper.MAIN_NOTIFICATION_CHANNEL_ID);

    }

    /**
     * Initializes the alarms depending on the settings
     */
    private void initializeAlarms() {
        //TODO: Check if user has disabled notifications for morning/evening
        //TODO: Check at what time the user wants to receive notifications
        Alarm alarm = new Alarm();
        long triggerAt = Utils.millisForAlarm(7, 0);
        alarm.setRepeatingAlarm(this, triggerAt, AlarmManager.INTERVAL_DAY, Alarm.REQUEST_CODE_MORNING, Alarm.TYPE_MORNING_ALARM);
        triggerAt = Utils.millisForAlarm(19, 0);
        alarm.setRepeatingAlarm(this, triggerAt, AlarmManager.INTERVAL_DAY, Alarm.REQUEST_CODE_EVENING, Alarm.TYPE_EVENING_ALARM);


        //NOTE: To change the timing (or rather anything about an alarm) it is necessary to first cancel the old alarm before setting the new one, otherwise android will not set a new alarm
        //NOTE: Setting PendingIntent to FLAG_UPDATE_CURRENT seems to fix this problem. Further testing is required
        //alarm.cancelAlarm(this, Alarm.REQUEST_CODE_MORNING, Alarm.TYPE_MORNING_ALARM);
        //alarm.cancelAlarm(this, Alarm.REQUEST_CODE_EVENING, Alarm.TYPE_EVENING_ALARM);



    }

    //TODO: pretty ugly, make it just pretty
    @Override
    public void onWeatherDataUpdated() {
        Weather weather = provider.getWeather();
        tempTV.setText(weather.getTemperature() + "°C");
        tempMinTV.setText("Min: " + weather.getMinTemp()  + "°C");
        tempMaxTV.setText("Max: " + weather.getMaxTemp()  + "°C");
        precipitationTV.setText("Niederschlag: " +(int) weather.getPrecipitation() + "%");
        Glide.with(this).load(weather.getImageURL()).into(weatherIcon);
    }
}