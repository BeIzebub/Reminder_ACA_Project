package com.reminder.social_activities.social_service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Gor on 3/25/2016.
 */
public class Sender extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String action = (String) getIntent().getExtras().get("do_action");
        if (action.equals("c")) {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            Intent i = getIntent();
            emailIntent.setType("message/rfc822");
            String[] s =  new String[]{(String) i.getExtras().get("to")};
            emailIntent.putExtra(Intent.EXTRA_EMAIL, s);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, (String) i.getExtras().get("subject"));
            emailIntent.putExtra(Intent.EXTRA_TEXT, (String) i.getExtras().get("text"));
            startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
        }
        finish();
    }
}
