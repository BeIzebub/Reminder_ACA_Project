package com.reminder.mobile_activities.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Armen on 20.03.2016.
 */
public class CallReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, CallService.class);
        i.putExtra("n", intent.getExtras().getString("n"));
        i.putExtra("id", intent.getExtras().getInt("id"));
        context.startService(i);
    }
}
