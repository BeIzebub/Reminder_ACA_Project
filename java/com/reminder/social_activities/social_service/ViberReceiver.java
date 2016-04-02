package com.reminder.social_activities.social_service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class ViberReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, ViberService.class);
        i.putExtra("text", intent.getExtras().getString("text"));
        i.putExtra("id", intent.getExtras().getInt("id"));
        context.startService(i);
    }
}
