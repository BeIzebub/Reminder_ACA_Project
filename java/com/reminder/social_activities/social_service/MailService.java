package com.reminder.social_activities.social_service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import com.reminder.R;

/**
 * Created by Gor on 3/25/2016.
 */
public class MailService extends IntentService {
    private String to;
    private String subject;
    private String text;
    public MailService() {
        super(MailService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        to = (String) intent.getExtras().get("to");
        text = (String) intent.getExtras().get("text");
        subject = (String) intent.getExtras().get("subject");
        createGmailReceiveNotification();

    }
    public void createGmailReceiveNotification() {
        Intent intent = new Intent(MailService.this, Sender.class);
        intent.putExtra("do_action", "c");
        intent.putExtra("to", to);
        intent.putExtra("text", text);
        intent.putExtra("subject", subject);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notif = new Notification.Builder(this)
                .setContentTitle("Time to send Gmail")
                .setContentText("Your Gmail is ready to be send")
                .setContentIntent(pendingIntent)
                .addAction(R.mipmap.ic_launcher, "Send", pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.defaults |= Notification.DEFAULT_SOUND;
        notif.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notif);
    }
}
