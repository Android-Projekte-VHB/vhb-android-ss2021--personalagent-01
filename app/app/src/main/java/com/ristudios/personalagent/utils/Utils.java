package com.ristudios.personalagent.utils;


import android.content.Context;

import androidx.annotation.IntRange;

import com.ristudios.personalagent.R;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Random;

public final class Utils {

    public static final String NOTIFICATION_MORNING_ENABLED = "notificationsMorningKey";
    public static final String NOTIFICATION_EVENING_ENABLED = "notificationsEveningKey";
    public static final String LOG_ALARM = "ALARM_KEY";
    public static final int TYPE_MORNING_MESSAGE = 558;
    public static final int TYPE_MORNING_TITLE = 559;
    public static final int TYPE_EVENING_MESSAGE = 560;
    public static final int TYPE_EVENING_TITLE = 561;


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


}
