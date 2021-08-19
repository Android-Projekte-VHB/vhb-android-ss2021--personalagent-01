package com.ristudios.personalagent.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompleteReceiver extends BroadcastReceiver {

    /**
     * Sets up alarms on device reboot if the user has enabled them.
     * @param context Application context.
     * @param intent The received intent.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            //TODO: Check if user has disabled Notifications
            Utils.setupAlarms(context);
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }
}