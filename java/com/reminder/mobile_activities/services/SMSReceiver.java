package com.reminder.mobile_activities.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Gor on 3/21/2016.
 */
public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, SMSService.class);
        i.putExtra("phone", intent.getExtras().getString("phone"));
        i.putExtra("text", intent.getExtras().getString("text"));
        i.putExtra("comment", intent.getExtras().getString("comment"));
        i.putExtra("id", intent.getExtras().getInt("id"));
        context.startService(i);
    }
}
