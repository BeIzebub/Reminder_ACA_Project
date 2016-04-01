package com.reminder.mobile_activities.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;

import com.reminder.R;

/**
 * Created by Armen on 01.04.2016.
 */
public class SimpleService extends IntentService {

    public SimpleService() {
        super(SimpleService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        createSMSReceiveNotification(intent.getExtras().getString("n"));
    }

    public void createSMSReceiveNotification(String s) {
        Notification notif = new Notification.Builder(this)
                .setContentTitle("Reminder")
                .setContentText(s)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notif.flags |= Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_AUTO_CANCEL;
        notif.ledARGB = Color.GREEN;
        notif.ledOnMS = 1000;
        notif.ledOffMS = 300;
        notif.defaults |= Notification.DEFAULT_SOUND;
        notif.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notif);
    }
}
