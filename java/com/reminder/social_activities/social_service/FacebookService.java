package com.reminder.social_activities.social_service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import com.reminder.R;

/**
 * Created by Karen on 25-Mar-16.
 */
public class FacebookService extends IntentService {

    private String text;
    private boolean isSent = true;

    public FacebookService() {
        super(FacebookService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        text = (String) intent.getExtras().get("text");
        Intent shareIntent = new Intent(FacebookService.this, FacebookSharer.class);
        shareIntent.putExtra("do_action", "sh");
        shareIntent.putExtra("text", text);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), shareIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            isSent = true;
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            isSent = false;
        }finally {
            if(isSent){
                createFacebookNotification();
            }else {
                createFacebookNotificationWithButton();
            }
        }
    }
    public void createFacebookNotification() {
        Notification notif = new Notification.Builder(this)
                .setContentTitle("Reminder")
                .setContentText("Your message is posted to Facebook")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.defaults |= Notification.DEFAULT_SOUND;
        notif.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notif);
    }

    public void createFacebookNotificationWithButton() {
        Intent intent = new Intent(FacebookService.this, FacebookSharer.class);
        intent.putExtra("do_action", "sh");
        intent.putExtra("text", text);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notif = new Notification.Builder(this)
                .setContentTitle("Reminder")
                .setContentText("An error occurred, please press Post")
                .setContentIntent(pendingIntent)
                .addAction(R.mipmap.ic_launcher, "Post", pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.defaults |= Notification.DEFAULT_SOUND;
        notif.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notif);
    }
}
