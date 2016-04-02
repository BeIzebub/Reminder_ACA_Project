package com.reminder.social_activities.social_service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.MediaPlayer;

import com.reminder.R;


public class ViberService extends IntentService {
    private String text;

    public ViberService() {
        super(ViberService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        MediaPlayer mediaPlayer=MediaPlayer.create(getApplicationContext(), R.raw.star_wars);
        mediaPlayer.start();

        createViberNotification();
    }
    public void createViberNotification() {
        Notification notif = new Notification.Builder(this)
                .setContentTitle("Reminder")
                .setContentText("Time to sent you Viber message")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.defaults |= Notification.DEFAULT_SOUND;
        notif.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notif);
    }
}
