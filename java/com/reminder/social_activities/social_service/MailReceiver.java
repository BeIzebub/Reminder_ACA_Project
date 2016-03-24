package com.reminder.social_activities.social_service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Gor on 3/25/2016.
 */
public class MailReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MailService.class);
        i.putExtra("to", intent.getExtras().getString("to"));
        i.putExtra("subject", intent.getExtras().getString("subject"));
        i.putExtra("text", intent.getExtras().getString("text"));
        context.startService(i);
    }
}
