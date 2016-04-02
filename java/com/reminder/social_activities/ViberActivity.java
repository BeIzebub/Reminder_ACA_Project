package com.reminder.social_activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.reminder.BaseActivity;
import com.reminder.R;

public class ViberActivity extends BaseActivity {

    private Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viber);

        setTitle("Viber");

        ok = (Button) findViewById(R.id.okay);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MediaPlayer mediaPlayer=MediaPlayer.create(ViberActivity.this, R.raw.star_wars);
                mediaPlayer.start();
            }
        });
    }
}
