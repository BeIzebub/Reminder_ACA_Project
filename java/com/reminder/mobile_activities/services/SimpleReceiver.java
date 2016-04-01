package com.reminder.mobile_activities.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Armen on 01.04.2016.
 */
public class SimpleReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, SimpleService.class);
        i.putExtra("n", intent.getExtras().getString("n"));
        context.startService(i);
    }
}
