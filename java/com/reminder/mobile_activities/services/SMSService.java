package com.reminder.mobile_activities.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.telephony.SmsManager;

import com.reminder.DAO.RemindersDB;
import com.reminder.R;

/**
 * Created by Gor on 3/21/2016.
 */
public class SMSService extends IntentService {
    private String phone;
    private String text;
    private String comment;

    public SMSService() {
        super(SMSService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        phone = (String) intent.getExtras().get("phone");
        text = (String) intent.getExtras().get("text");
        comment = (String) intent.getExtras().get("comment");
        SmsManager manager = SmsManager.getDefault();
        manager.sendTextMessage(phone, null, text, null, null);
        createSMSReceiveNotification(phone);
        RemindersDB.getInstance(this).deleteSmsReminder(intent.getExtras().getInt("id"));
    }

    public void createSMSReceiveNotification(String s) {
        String str;
        if (comment==null) {
            str = "Your SMS to " + s + " was sent successfully";
        } else {
            str = "Your SMS to " + s + " was sent successfully: " + comment;
        }
        Notification notif = new Notification.Builder(this)
                .setContentTitle("SMS successful sent")
                .setContentText(str)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.defaults |= Notification.DEFAULT_SOUND;
        notif.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notif);
    }
}