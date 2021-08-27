package com.ristudios.personalagent.utils;


import android.content.Context;

import androidx.annotation.IntRange;

import com.ristudios.personalagent.R;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalField;
import java.util.Random;

public final class Utils {

    public static final String LOG_ALARM = "ALARM_KEY";
    public static final int TYPE_MORNING_MESSAGE = 558;
    public static final int TYPE_MORNING_TITLE = 559;
    public static final int TYPE_EVENING_MESSAGE = 560;
    public static final int TYPE_EVENING_TITLE = 561;
    public static final int REQUEST_LOCATION_PERMISSIONS = 10;

    //region SharedPreferences Keys

    public static final String SP_NOTIFICATION_ENABLED_KEY = "notification_switch";
    public static final String SP_NOTIFICATION_TIME_ONE_KEY = "notification_time_one";
    public static final String SP_NOTIFICATION_TIME_TWO_KEY = "notification_time_two";
    public static final String SP_LOCATION_PERMISSION_KEY = "permission_switch";
    public static final String SP_USERNAME_KEY = "username";
    public static final String SP_WEEKLY_GOAL_KEY = "weekly_point_goal";
    public static final String SP_NOTIFICATION_TIME_ONE_HOUR_KEY = "notification_time_one_hour";
    public static final String SP_NOTIFICATION_TIME_TWO_HOUR_KEY = "notification_time_two_hour";
    public static final String SP_NOTIFICATION_TIME_ONE_MINUTE_KEY = "notification_time_one_minute";
    public static final String SP_NOTIFICATION_TIME_TWO_MINUTE_KEY = "notification_time_two_minute";

    //endregion

    //region java.time converters and utils

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
     * Converts hour and minute to the epoch milli for the current day, used to get the time for an
     * entry.
     * @param hour The hour of the entry.
     * @param minute The minute of the entry.
     * @return The milliseconds representing today at the time hour:minute.
     */
    public static long millisForEntry(int hour, int minute)
    {
        ZonedDateTime currentTime = Utils.getCurrentZonedTime();
        LocalTime specific = LocalTime.of(hour, minute);
        return currentTime.with(specific).toInstant().toEpochMilli();
    }

    /**
     * Converts milliseconds into a ZonedDateTime, used for converting the long date of entries back to
     * an actual date
     * @param millis the milliseconds of the entry
     * @return ZonedDateTime representing the time of the entry
     */
    public static ZonedDateTime getDateFromMillis(long millis){
        ZonedDateTime date = ZonedDateTime.now(ZoneId.systemDefault());
        return date.with(Instant.ofEpochMilli(millis));
    }

    /**
     * Formats a ZonedDateTime to display hours and minutes.
     * @param zonedDateTime The Date to be formatted.
     * @return Formatted String HH:mm
     */
    public static String getFormattedTime(ZonedDateTime zonedDateTime)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return formatter.format(zonedDateTime);
    }

    /**
     * Formats a ZonedDateTime to display the current date
     * @param zonedDateTime The Date to be formatted.
     * @return Formatted String dd.MM.yyyy
     */
    public static String getFormattedDate(ZonedDateTime zonedDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return formatter.format(zonedDateTime);
    }

    /**
     * Formats a ZonedDateTime to display the current date with time
     * @param zonedDateTime The Date to be formatted.
     * @return Formatted String dd.MM.yyyy HH:mm
     */
    public static String getFormattedDateTime(ZonedDateTime zonedDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return formatter.format(zonedDateTime);
    }

    //endregion


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
