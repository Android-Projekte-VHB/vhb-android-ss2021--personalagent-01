package com.ristudios.personalagent.utils;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.ristudios.personalagent.utils.notifications.Alarm;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            if (prefs.getBoolean(Utils.SP_NOTIFICATION_ENABLED_KEY, true)){
                Alarm alarm = new Alarm();
                alarm.setRepeatingAlarm(context, Utils.millisForAlarm(prefs.getInt(Utils.SP_NOTIFICATION_TIME_ONE_HOUR_KEY, 7),
                        prefs.getInt(Utils.SP_NOTIFICATION_TIME_ONE_MINUTE_KEY, 0)), AlarmManager.INTERVAL_DAY,
                        Alarm.REQUEST_CODE_MORNING, Alarm.TYPE_MORNING_ALARM);
                alarm.setRepeatingAlarm(context, Utils.millisForAlarm(prefs.getInt(Utils.SP_NOTIFICATION_TIME_TWO_HOUR_KEY, 7),
                        prefs.getInt(Utils.SP_NOTIFICATION_TIME_TWO_MINUTE_KEY, 0)), AlarmManager.INTERVAL_DAY,
                        Alarm.REQUEST_CODE_EVENING, Alarm.TYPE_EVENING_ALARM);
                Log.d(Utils.LOG_ALARM, "Boot complete received, alarms will be set");
            }
            else{
                Log.d(Utils.LOG_ALARM, "Boot complete received, no alarms will be set");
            }
        }
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}