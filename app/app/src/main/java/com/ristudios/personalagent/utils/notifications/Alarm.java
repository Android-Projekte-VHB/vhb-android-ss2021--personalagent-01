package com.ristudios.personalagent.utils.notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ristudios.personalagent.ui.activities.MainActivity;
import com.ristudios.personalagent.R;
import com.ristudios.personalagent.utils.Utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Alarm extends BroadcastReceiver {

    public static final String TYPE_MORNING_ALARM = "com.ristudios.ALARM_MORNING_TRIGGERED";
    public static final String TYPE_EVENING_ALARM = "com.ristudios.ALARM_EVENING_TRIGGERED";
    public static final int REQUEST_CODE_MORNING = 99;
    public static final int REQUEST_CODE_EVENING = 199;

    /**
     * Creates a new instance of the class Alarm.
     */
    public Alarm()
    {

    }

    /**
     * Shows a notification, the content of the Notification is based on the intent action.
     * @param context Application context.
     * @param intent The Intent calling this receiver.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        Intent sender = new Intent(context, MainActivity.class);
        Log.d(Utils.LOG_ALARM, "Alarm received!");
        if (intent.getAction().equals(Alarm.TYPE_MORNING_ALARM)){
            String title = Utils.getRandomNotificationString(context, Utils.TYPE_MORNING_TITLE);
            String message = Utils.getRandomNotificationString(context, Utils.TYPE_MORNING_MESSAGE);
            PendingIntent pendingIntent = notificationHelper.createContentIntent(sender, NotificationHelper.NOTIFICATION_REQUEST_CODE_MORNING);
            Notification notification = notificationHelper.createNotification(title, message, R.drawable.ic_test_24dp, NotificationHelper.AUTOCANCEL, pendingIntent);
            notificationHelper.showNotification(NotificationHelper.MORNING_NOTIFICATION_ID, notification);
            Log.d(Utils.LOG_ALARM, "Alarm for morning!");
        }
        else if (intent.getAction().equals(Alarm.TYPE_EVENING_ALARM)){
            String title = Utils.getRandomNotificationString(context, Utils.TYPE_EVENING_TITLE);
            String message = Utils.getRandomNotificationString(context, Utils.TYPE_EVENING_MESSAGE);
            PendingIntent pendingIntent = notificationHelper.createContentIntent(sender, NotificationHelper.NOTIFICATION_REQUEST_CODE_EVENING);
            Notification notification = notificationHelper.createNotification(title, message, R.drawable.ic_test_24dp, NotificationHelper.AUTOCANCEL, pendingIntent);
            notificationHelper.showNotification(NotificationHelper.EVENING_NOTIFICATION_ID, notification);
            Log.d(Utils.LOG_ALARM, "Alarm for evening!");
        }
    }

    /**
     * Sets a repeating Alarm that calls onReceive of Alarm.
     * @param context Application context
     * @param triggerAt The time at which the Alarm is triggered for the first time.
     * @param interval The interval in which the alarm is repeated.
     * @param requestCode The requestCode of the Alarm. <p><font color = "orange"> Should use a constant defined in Alarm.class.</font></p>
     * @param type The type of the Alarm. <p><font color = "orange"> Should use a constant defined in Alarm.class.</font></p>
     */
    public void setRepeatingAlarm(Context context, long triggerAt, long interval, int requestCode, String type)
    {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Alarm.class);
        intent.setAction(type);
        PendingIntent sender = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAt, interval, sender);
    }

    /**
     * Cancels an Alarm. The requestCode of the Alarm to be cancelled as well as the type must match with
     * the requestCode and type of the Alarm.
     * @param context Application context.
     * @param requestCode The requestCode of the Alarm.<p><font color = "orange"> Should use a constant defined in Alarm.class.</font></p>
     * @param type The type of the Alarm. <p><font color = "orange"> Should use a constant defined in Alarm.class.</font></p>
     */
    public void cancelAlarm(Context context, int requestCode,String type)
    {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Alarm.class);
        intent.setAction(type);
        PendingIntent sender = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        alarmManager.cancel(sender);
    }



}
