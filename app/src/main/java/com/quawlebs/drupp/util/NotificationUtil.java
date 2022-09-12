package com.quawlebs.drupp.util;

import android.app.NotificationManager;
import android.content.Context;

public class NotificationUtil {
    private NotificationUtil() {

    }

    public static void removeAllNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

}
