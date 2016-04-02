package com.reminder.social_activities.social_service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.reminder.R;

/**
 * Created by Karen on 02-Apr-16.
 */
public class ViberSender extends Activity{

    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final MediaPlayer mediaPlayer=MediaPlayer.create(getApplicationContext(), R.raw.star_wars);
        mediaPlayer.start();

        text = (String) getIntent().getExtras().get("text");

        AlertDialog.Builder builder = new AlertDialog.Builder(ViberSender.this);

        builder.setTitle("Reminder");
        builder.setMessage("Time to send your Viber Message");

        builder.setPositiveButton("SEND", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, text);
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.viber.voip");
                startActivity(sendIntent);

                mediaPlayer.stop();

                dialog.dismiss();
            }

        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
