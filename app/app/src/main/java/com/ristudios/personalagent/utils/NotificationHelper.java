package com.ristudios.personalagent.utils;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/**
 * Helper class that handles Notifications.
 */
public class NotificationHelper {

    private Context context;
    public static final boolean AUTOCANCEL = true;
    public static final boolean NO_AUTOCANCEL = false;
    public static final String MAIN_NOTIFICATION_CHANNEL_ID = "allNotifications";


    public NotificationHelper(Context context) {
        this.context = context;
    }

    /**
     * Creates a new {@link Notification}.
     * @param title the title of the notification
     * @param content the content of the notification
     * @param icon the icon of the notification
     * @param autoCancel set autoCancel of notification
     * @param pendingIntent set pendingIntent to launch an activity when the user clicks the notification
     * @return new Notification
     */
    public Notification createNotification(String title, String content, int icon, boolean autoCancel, @Nullable PendingIntent pendingIntent)
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MAIN_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        if (autoCancel) {
            builder.setAutoCancel(true);
        }
        if (pendingIntent != null){
            builder.setContentIntent(pendingIntent);
        }

        return builder.build();
    }

    /**
     * Displays a {@link Notification} to the user.
     * @param id the id of the notification
     * @param notification the notification to display
     */
    public void showNotification(int id, Notification notification)
    {
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(id, notification);
    }

    /**
     * Creates a PendingIntent that can be set on a notification to do something when clicked.
     * @param intent the Intent used to create the PendingIntent
     * @param requestCode the requestCode of the PendingIntent
     * @return new PendingIntent
     */
    public PendingIntent createContentIntent(Intent intent, int requestCode)
    {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntentWithParentStack(intent);
        return taskStackBuilder.getPendingIntent(requestCode, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Creates a new NotificationChannel. This is needed on Android Oreo (SDK VERSION 26) or higher
     * in order to display notifications
     * @param context applicationContext
     * @param name name of the channel
     * @param description description of the channel
     * @param id id of the channel
     */
    public void createNotificationChannel(Context context, String name, String description, String id)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(id, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /*
     *┈┈┈╲┈┈┈┈╱┈┈┈┈┈┈┈
     * ┈┈┈╱▔▔▔▔╲┈┈┈┈┈┈┈
     * ┈┈┃┈▇┈┈▇┈┃┈┈┈┈┈┈
     * ╭╮┣━━━━━━┫╭╮┈┈┈┈
     * ┃┃┃┈┈┈┈┈┈┃┃┃┈┈┈┈
     * ╰╯┃┈┈┈┈┈┈┃╰╯┈┈┈┈
     * ┈┈╰┓┏━━┓┏╯┈┈┈┈┈┈
     * ┈┈┈╰╯┈┈╰╯┈
     */

}
