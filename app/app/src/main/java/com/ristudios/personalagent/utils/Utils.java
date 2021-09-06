package com.ristudios.personalagent.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.IntRange;

import com.ristudios.personalagent.R;
import com.ristudios.personalagent.data.Category;
import com.ristudios.personalagent.data.Entry;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public final class Utils {

    public static final String LOG_ALARM = "ALARM_KEY";
    public static final int TYPE_MORNING_MESSAGE = 558;
    public static final int TYPE_MORNING_TITLE = 559;
    public static final int TYPE_EVENING_MESSAGE = 560;
    public static final int TYPE_EVENING_TITLE = 561;
    public static final int REQUEST_LOCATION_PERMISSIONS = 10;
    public static final int FACTOR_GOAL_PER_WEEK = 7;
    public static final float FACTOR_LIFE_BALANCE = 0.25f; //hobby and fitness
    public static final float FACTOR_WORK_BALANCE = 0.5f;

    //region SharedPreferences Keys

    public static final String SP_NOTIFICATION_ENABLED_KEY = "notification_switch";
    public static final String SP_NOTIFICATION_TIME_ONE_KEY = "notification_time_one";
    public static final String SP_NOTIFICATION_TIME_TWO_KEY = "notification_time_two";
    public static final String SP_LOCATION_PERMISSION_KEY = "permission_switch";
    public static final String SP_USERNAME_KEY = "username";
    public static final String SP_DAILY_GOAL_KEY = "daily_point_goal";
    public static final String SP_WORK_TOTAL_POINTS_KEY = "work_total";
    public static final String SP_HOBBY_TOTAL_POINTS_KEY = "hobby_total";
    public static final String SP_FITNESS_TOTAL_POINTS_KEY = "fitness_total";
    public static final String SP_NOTIFICATION_TIME_ONE_HOUR_KEY = "notification_time_one_hour";
    public static final String SP_NOTIFICATION_TIME_TWO_HOUR_KEY = "notification_time_two_hour";
    public static final String SP_NOTIFICATION_TIME_ONE_MINUTE_KEY = "notification_time_one_minute";
    public static final String SP_NOTIFICATION_TIME_TWO_MINUTE_KEY = "notification_time_two_minute";

    //endregion

    //region java.time converters and utils

    /**
     * Returns the current time considering the systems default timezone.
     *
     * @return current Time (as ZonedDateTime).
     */
    public static ZonedDateTime getCurrentZonedTime() {
        return ZonedDateTime.now(ZoneId.systemDefault());
    }

    public static long millisForReset(){
        ZonedDateTime currentZonedTime = Utils.getCurrentZonedTime();
        DayOfWeek dayOfWeek = currentZonedTime.getDayOfWeek();
        while(dayOfWeek != DayOfWeek.MONDAY){
            currentZonedTime = currentZonedTime.plusDays(1);
            dayOfWeek = currentZonedTime.getDayOfWeek();
        }
        currentZonedTime.with(LocalTime.of(0,0,0));
        return currentZonedTime.toInstant().toEpochMilli();
    }

    /**
     * Returns the time (in milliseconds) at a specific point in the future that can be passed to AlarmManager.
     * If the specified time of the day has already passed today, returns the value of that time for the next day, else
     * returns the value of that time for today dependant on the systems default timezone.
     *
     * @param hour   The hour of the specified time.
     * @param minute The minute of the specified time.
     * @return the milliseconds of the specified time, starting from epoch (Jan. 1st 1970, 00:00:00 localTime)
     * <p>
     * Credit to stackoverflow user Basil Bourque for providing code at stackoverflow.
     * See <a href="https://stackoverflow.com/questions/68840037/java-how-to-get-specific-point-in-the-future-in-millis">
     * https://stackoverflow.com/questions/68840037/java-how-to-get-specific-point-in-the-future-in-millis
     * </a>
     */
    public static long millisForAlarm(@IntRange(from = 0, to = 23) int hour, @IntRange(from = 0, to = 59) int minute) {
        ZonedDateTime currentZonedTime = Utils.getCurrentZonedTime();
        LocalTime specificTime = LocalTime.of(hour, minute);
        ZonedDateTime alarmTime = currentZonedTime;
        if (currentZonedTime.toLocalTime().isBefore(specificTime)) {
            alarmTime = currentZonedTime.with(specificTime);
        } else if (currentZonedTime.toLocalTime().isAfter(specificTime)) {
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
    public static long millisForEntryCurrentDay(int hour, int minute)
    {
        ZonedDateTime currentTime = Utils.getCurrentZonedTime();
        LocalTime specific = LocalTime.of(hour, minute);
        return currentTime.with(specific).toInstant().toEpochMilli();
    }

    /**
     * Converts date and time (as year, month, day, hour, minute) to the epoch millis representing that time.
     * @param year Year of date.
     * @param month Month of date.
     * @param day Day of month.
     * @param hour Hour of day.
     * @param minute Minute of day.
     * @return The milliseconds representing the specified date.
     */
    public static long millisForEntryWithDate(int year, int month, int day, int hour, int minute){
        ZonedDateTime currentTime = Utils.getCurrentZonedTime();
        ZonedDateTime targetDateTime = currentTime.with(LocalDate.of(year, month, day));
        targetDateTime = targetDateTime.with(LocalTime.of(hour, minute));
        return targetDateTime.toInstant().toEpochMilli();
    }

    /**
     * Converts the current date at 00:00:00 and 23:59:59 to millis that can then be used to
     * search for entries of teh current day.
     * @return long array with starttime and endtime.
     */
    public static long[] getSearchTimesForToday()
    {
        ZonedDateTime zonedDateTime = Utils.getCurrentZonedTime();
        LocalTime startOfDay = LocalTime.of(0,0,0);
        LocalTime endOfDay = LocalTime.of(23, 59, 59);
        long startMillis = zonedDateTime.with(startOfDay).toInstant().toEpochMilli();
        long endMillis = zonedDateTime.with(endOfDay).toInstant().toEpochMilli();
        return new long[] {startMillis , endMillis};
    }

    /**
     * Converts a specified date (in year, month, day) at 00:00:00 and 23:59:59 to millis that can then be used to search for
     * entries of a specific day.
     * @param year The year of the date.
     * @param month The month of the date.
     * @param day The day of the date.
     * @return long array with starttime and endtime for the specified date.
     */
    public static long[] getSearchTimesForDate(int year, int month, int day)
    {
        LocalDate date = LocalDate.of(year, month, day);
        LocalTime startOfDay = LocalTime.of(0,0,0);
        LocalTime endOfDay = LocalTime.of(23, 59, 59);
        long startMillis = ZonedDateTime.of(date, startOfDay, ZoneId.systemDefault()).toInstant().toEpochMilli();
        long endMillis = ZonedDateTime.of(date, endOfDay, ZoneId.systemDefault()).toInstant().toEpochMilli();
        return new long[] {startMillis, endMillis};
    }

    /**
     * Converts two dates to long millis with time of date one being 00:00:00 and time of
     * date two being 23:59:59. These can then be used to search for all items that are in the specified timespan.
     * @param yearOne Year of date one.
     * @param monthOne Month of date one.
     * @param dayOne Day of date one.
     * @param yearTwo Year of date two.
     * @param monthTwo Month of date two.
     * @param dayTwo Day of date two.
     * @return long array with starttime and endtime.
     */
    public static long[] getSearchTimesForTimeSpan(int yearOne, int monthOne, int dayOne, int yearTwo, int monthTwo, int dayTwo)
    {
        LocalDate startDate = LocalDate.of(yearOne, monthOne, dayOne);
        LocalDate endDate = LocalDate.of(yearTwo, monthTwo, dayTwo);
        LocalTime startOfDay = LocalTime.of(0, 0, 0);
        LocalTime endOfDay = LocalTime.of(23, 59, 59);
        long startMillis = ZonedDateTime.of(startDate, startOfDay, ZoneId.systemDefault()).toInstant().toEpochMilli();
        long endMillis = ZonedDateTime.of(endDate, endOfDay, ZoneId.systemDefault()).toInstant().toEpochMilli();
        return new long[] {startMillis, endMillis};
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
     * Formats a ZonedDateTime to display the current date in the default local format of the device.
     * @param zonedDateTime Date to be formatted.
     * @return Formatted string in local default format.
     */
    public static String getLocalizedFormattedDate(ZonedDateTime zonedDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
        return formatter.format(zonedDateTime);
    }



    //endregion


    /**
     * <p><font color="red">WARNING: MORE TESTING REQUIRED</font></p>
     *
     *Sorts an Arraylist by Category to make the ui look better.
     * @param toSort The arraylist that will be sorted.
     * @return The sorted arraylist.
     */
    public static ArrayList<Entry> sortListByCategory(List<Entry> toSort){
        ArrayList<Entry> sortedList = new ArrayList<>();
        for (Entry entry : toSort){
                if (entry.getCategory().equals(Category.WORK))
                {
                    sortedList.add(entry);
                }
        }
        for (Entry entry : toSort){
            if (entry.getCategory().equals(Category.HOBBY))
            {
                sortedList.add(entry);
            }
        }
        for (Entry entry : toSort){
            if (entry.getCategory().equals(Category.FITNESS))
            {
                sortedList.add(entry);
            }
        }
        for (Entry entry : toSort){
            if (entry.getCategory().equals(Category.APPOINTMENT))
            {
                sortedList.add(entry);
            }
        }
        return sortedList;
    }

    /**
     * Returns a random String for notifications.
     * @param context ApplicationContext
     * @param type Type of the Notification.
     * @return Random String.
     */
    public static String getRandomNotificationString(Context context, int type) {
        String rndString = "";
        if (type == TYPE_MORNING_TITLE) {
            rndString = getRandomString(context, R.array.morning_notification_titles);
        }
        if (type == TYPE_MORNING_MESSAGE) {
            rndString = getRandomString(context, R.array.morning_notification_messages);
        }
        if (type == TYPE_EVENING_TITLE) {
            rndString = getRandomString(context, R.array.evening_notification_titles);
        }
        if (type == TYPE_EVENING_MESSAGE) {
            rndString = getRandomString(context, R.array.evening_notification_messages);
        }
        return rndString;

    }

    /**
     * Returns a random String chosen from one of 4 arrays to change the notification messages randomly.
     * @param context ApplicationContext.
     * @param array The array to choose from.
     * @return Random String.
     */
    private static String getRandomString(Context context, int array) {
        Random random = new Random();
        String randomString = "";
        String[] strings = context.getResources().getStringArray(array);
        switch (random.nextInt(strings.length)) {
            case 0:
                randomString = strings[0];
                break;
            case 1:
                randomString = strings[1];
                break;
            case 2:
                randomString = strings[2];
            default:
                break;
        }
        return randomString;
    }

    /**
     * Switches the name of a month with a string resource to display text in the correct language.
     * @param name original name of month (by month.name()).
     * @param context ApplicationContext.
     * @return the name of month from stringRes.
     */
    public static String getLocalMonthName(String name, Context context){
        String localName = "";
        String[] months = context.getResources().getStringArray(R.array.months);
        switch (name){
            case "january": localName = months[0];
                break;
            case "february": localName = months[1];
                break;
            case "march":localName = months[2];
                break;
            case "april":localName = months[3];
                break;
            case "may":localName = months[4];
                break;
            case "june":localName = months[5];
                break;
            case "july":localName = months[6];
                break;
            case "august":localName = months[7];
                break;
            case "september":localName = months[8];
                break;
            case "october":localName = months[9];
                break;
            case "november":localName = months[10];
                break;
            case "december":localName = months[11];
                break;
        }
        return localName;
    }

    /**
     * Converts a drawable into a bitmap so it can be used in methods which require bitmap objects.
     * @param drawable The drawable to convert.
     * @return Bitmap object displaying the drawable.
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }





}
