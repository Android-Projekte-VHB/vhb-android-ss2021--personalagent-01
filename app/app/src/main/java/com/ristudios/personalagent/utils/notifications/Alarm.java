package com.ristudios.personalagent.utils.notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.ristudios.personalagent.ui.activities.MainActivity;
import com.ristudios.personalagent.R;
import com.ristudios.personalagent.ui.activities.WeeklyOverviewActivity;
import com.ristudios.personalagent.utils.Utils;

public class Alarm extends BroadcastReceiver {

    public static final String TYPE_MORNING_ALARM = "com.ristudios.ALARM_MORNING_TRIGGERED";
    public static final String TYPE_EVENING_ALARM = "com.ristudios.ALARM_EVENING_TRIGGERED";
    public static final String TYPE_RESET_ALARM = "com.ristudios.ALARM_RESET_TRIGGERED";
    public static final int REQUEST_CODE_MORNING = 99;
    public static final int REQUEST_CODE_EVENING = 199;
    public static final int REQUEST_CODE_RESET = 7;

    /**
     * Creates a new instance of the class Alarm.
     */
    public Alarm() {

    }

    /**
     * Shows a notification, the content of the Notification is based on the intent action.
     *
     * @param context Application context.
     * @param intent  The Intent calling this receiver.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);

        if (intent.getAction().equals(Alarm.TYPE_MORNING_ALARM)) {
            Intent sender = new Intent(context, MainActivity.class);
            String title = Utils.getRandomNotificationString(context, Utils.TYPE_MORNING_TITLE);
            String message = Utils.getRandomNotificationString(context, Utils.TYPE_MORNING_MESSAGE);
            PendingIntent pendingIntent = notificationHelper.createContentIntent(sender, NotificationHelper.NOTIFICATION_REQUEST_CODE_MORNING);
            Notification notification = notificationHelper.createNotification(title, message, R.drawable.android_mascot_30dp, NotificationHelper.AUTOCANCEL, pendingIntent);
            notificationHelper.showNotification(NotificationHelper.MORNING_NOTIFICATION_ID, notification);
        } else if (intent.getAction().equals(Alarm.TYPE_EVENING_ALARM)) {
            Intent sender = new Intent(context, MainActivity.class);
            String title = Utils.getRandomNotificationString(context, Utils.TYPE_EVENING_TITLE);
            String message = Utils.getRandomNotificationString(context, Utils.TYPE_EVENING_MESSAGE);
            PendingIntent pendingIntent = notificationHelper.createContentIntent(sender, NotificationHelper.NOTIFICATION_REQUEST_CODE_EVENING);
            Notification notification = notificationHelper.createNotification(title, message, R.drawable.android_mascot_30dp, NotificationHelper.AUTOCANCEL, pendingIntent);
            notificationHelper.showNotification(NotificationHelper.EVENING_NOTIFICATION_ID, notification);

        } else if (intent.getAction().equals(Alarm.TYPE_RESET_ALARM)) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            int points = prefs.getInt(Utils.SP_FITNESS_TOTAL_POINTS_KEY, 0) + prefs.getInt(Utils.SP_WORK_TOTAL_POINTS_KEY, 0) + prefs.getInt(Utils.SP_HOBBY_TOTAL_POINTS_KEY, 0);
            int goal = prefs.getInt(Utils.SP_DAILY_GOAL_KEY, 0) * 7;
            String message = context.getString(R.string.notif_reset_message).replace("$AMOUNT", String.valueOf(points));
            if (points >= goal) {
                message = message.replace("$NOT", "");
            } else {
                message = message.replace("$NOT", context.getString(R.string.notif_not));
            }
            PendingIntent pendingIntent = notificationHelper.createContentIntent(new Intent(context, WeeklyOverviewActivity.class), 13);
            Notification notification = notificationHelper.createNotification(context.getString(R.string.notif_reset_title),
                    message, R.drawable.android_mascot_30dp, true, pendingIntent);
            notificationHelper.showNotification(-12, notification);
            prefs.edit().putInt(Utils.SP_FITNESS_TOTAL_POINTS_KEY, 0).putInt(Utils.SP_WORK_TOTAL_POINTS_KEY, 0).putInt(Utils.SP_HOBBY_TOTAL_POINTS_KEY, 0).apply();

        }
    }

    /**
     * Sets a repeating Alarm that calls onReceive of Alarm.
     *
     * @param context     Application context
     * @param triggerAt   The time at which the Alarm is triggered for the first time.
     * @param interval    The interval in which the alarm is repeated.
     * @param requestCode The requestCode of the Alarm. <p><font color = "orange"> Should use a constant defined in Alarm.class.</font></p>
     * @param type        The type of the Alarm. <p><font color = "orange"> Should use a constant defined in Alarm.class.</font></p>
     */
    public void setRepeatingAlarm(Context context, long triggerAt, long interval, int requestCode, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Alarm.class);
        intent.setAction(type);
        PendingIntent sender = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAt, interval, sender);
    }

    /**
     * Cancels an Alarm. The requestCode of the Alarm to be cancelled as well as the type must match with
     * the requestCode and type of the Alarm.
     *
     * @param context     Application context.
     * @param requestCode The requestCode of the Alarm.<p><font color = "orange"> Should use a constant defined in Alarm.class.</font></p>
     * @param type        The type of the Alarm. <p><font color = "orange"> Should use a constant defined in Alarm.class.</font></p>
     */
    public void cancelAlarm(Context context, int requestCode, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Alarm.class);
        intent.setAction(type);
        PendingIntent sender = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        alarmManager.cancel(sender);
    }


}
