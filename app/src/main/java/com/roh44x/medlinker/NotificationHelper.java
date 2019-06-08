package com.roh44x.medlinker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;

public class NotificationHelper extends ContextWrapper {


    private static final String OUR_CHANNEL_ID = "com.roh44x.medlinker.H44X";
    private static final String CHANNEL_NAME = "H44X";
    private NotificationManager manager;
    public NotificationHelper(Context base) {
        super(base);
    }

}
