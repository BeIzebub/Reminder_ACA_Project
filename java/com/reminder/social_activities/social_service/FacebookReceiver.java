package com.reminder.social_activities.social_service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Karen on 25-Mar-16.
 */
public class FacebookReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, FacebookService.class);
        i.putExtra("text", intent.getExtras().getString("text"));
        i.putExtra("id", intent.getExtras().getInt("id"));
        context.startService(i);
    }
}
