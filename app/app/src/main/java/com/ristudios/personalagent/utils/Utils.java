package com.ristudios.personalagent.utils;


import android.app.AlarmManager;
import android.content.Context;

import androidx.annotation.IntRange;

import com.ristudios.personalagent.R;
import com.ristudios.personalagent.utils.notifications.Alarm;
import com.ristudios.personalagent.utils.notifications.NotificationHelper;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Random;

public final class Utils {

    public static final String NOTIFICATION_MORNING_ENABLED = "notificationsMorningKey";
    public static final String NOTIFICATION_EVENING_ENABLED = "notificationsEveningKey";

    //all int constants are given random ints.
    public static final int TYPE_MORNING_MESSAGE = 50;
    public static final int TYPE_MORNING_TITLE = 51;
    public static final int TYPE_EVENING_MESSAGE = 60;
    public static final int TYPE_EVENING_TITLE = 61;


    //region alarm-related


    /**
     * Returns the current time considering the systems default timezone.
     * @return current Time (as ZonedDateTime).
     */
    public static ZonedDateTime getCurrentZonedTime()
    {
        return ZonedDateTime.now(ZoneId.systemDefault());
    }


    /**
     * Returns the time (in milliseconds) at a specific point in the future that can be passed to AlarmManager.
     * If the specified time of the day has already passed today, returns the value of that time for the next day, else
     * returns the value of that time for today dependant on the systems default timezone.
     * @param hour The hour of the specified time.
     * @param minute The minute of the specified time.
     * @return the milliseconds of the specified time, starting from epoch (Jan. 1st 1970, 00:00:00 localTime)
     *
     * Credit to stackoverflow user Basil Bourque for providing code at stackoverflow.
     * See <a href="https://stackoverflow.com/questions/68840037/java-how-to-get-specific-point-in-the-future-in-millis">
     *     https://stackoverflow.com/questions/68840037/java-how-to-get-specific-point-in-the-future-in-millis
     *     </a>
     */
    public static long millisForAlarm(@IntRange(from = 0, to = 23) int hour,@IntRange(from = 0, to = 59) int minute){
        ZonedDateTime currentZonedTime = Utils.getCurrentZonedTime();
        LocalTime specificTime = LocalTime.of(hour,minute);
        ZonedDateTime alarmTime = currentZonedTime;
        if (currentZonedTime.toLocalTime().isBefore(specificTime))
        {
            alarmTime = currentZonedTime.with(specificTime);
        }
        else if (currentZonedTime.toLocalTime().isAfter(specificTime))
        {
            alarmTime = currentZonedTime.toLocalDate().plusDays(1).atStartOfDay(ZoneId.systemDefault()).with(specificTime);

        }

        long timeTillAlarm = Duration.between(currentZonedTime.toInstant(), alarmTime.toInstant()).toMillis();
        return currentZonedTime.toInstant().toEpochMilli() + timeTillAlarm;
    }

    /**
     * Sets up alarms depending on the settings.
     * @param context Application context.
     */
    public static void setupAlarms(Context context){
        Alarm alarm = new Alarm();

        //TODO: Adjust time according to user selection, check if user has disabled Notifications
        long triggerAt = Utils.millisForAlarm(7, 0);
        alarm.setRepeatingAlarm(context, triggerAt, AlarmManager.INTERVAL_DAY, Alarm.REQUEST_CODE_MORNING, Alarm.TYPE_MORNING_ALARM);
        triggerAt = Utils.millisForAlarm(19, 0);
        alarm.setRepeatingAlarm(context, triggerAt, AlarmManager.INTERVAL_DAY, Alarm.REQUEST_CODE_EVENING, Alarm.TYPE_EVENING_ALARM);
    }

    /**
     * Cancels an alarm specified by requestCode and type.
     * @param context Application context.
     * @param requestCode The requestCode of the Alarm.<p><font color = "orange"> Should use a constant defined in Alarm.class.</font></p>
     * @param type The type of the Alarm. <p><font color = "orange"> Should use a constant defined in Alarm.class.</font></p>
     */
    public static void cancelAlarm(Context context, int requestCode, String type)
    {
        Alarm alarm = new Alarm();
        alarm.cancelAlarm(context, requestCode, type);
    }

    //endregion

    //region notification-related

    /**
     * Returns a random string that is used for a notification title or message.
     * @param context Application context.
     * @param type Type of string (e.g. a title for a notification in the morning)
     * @return random String
     */
    public static String getRandomNotificationString(Context context, int type) {

        String rndString = "";
        if (type == TYPE_MORNING_TITLE) {
            rndString = getRandomString(context, rndString, R.array.morning_notification_titles);
        }
        if (type == TYPE_MORNING_MESSAGE) {
            rndString = getRandomString(context, rndString, R.array.morning_notification_messages);
        }
        if (type == TYPE_EVENING_TITLE) {
            rndString = getRandomString(context, rndString, R.array.evening_notification_titles);
        }
            if (type == TYPE_EVENING_MESSAGE) {
                rndString = getRandomString(context, rndString, R.array.evening_notification_messages);
            }
            return rndString;

        }

    /**
     * Returns a random string dependant on the chosen array.
     * @param context Application context.
     * @param rndString the String to return
     * @param p the id of the array
     * @return random String from array p
     */
        private static String getRandomString (Context context, String rndString, int p){
            Random random = new Random();

            String[] strings = context.getResources().getStringArray(p);
            switch (random.nextInt(strings.length)) {
                case 0:
                    rndString = strings[0];
                    break;
                case 1:
                    rndString = strings[1];
                    break;
                case 2:
                    rndString = strings[2];
                default:
                    break;
            }
            return rndString;
        }

    /**
     * Sends a simple notification, used for bugfixing and testing.
     * @param context Application context.
     */
    public static void sendTest(Context context){
        NotificationHelper notificationHelper = new NotificationHelper(context);
        notificationHelper.showNotification(-10, notificationHelper.createNotification("title", "test", R.drawable.ic_test_24dp, false, null));
    }

    //endregion





}
