package com.reminder.social_activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.MessageDialog;
import com.facebook.share.widget.ShareDialog;
import com.reminder.BaseActivity;
import com.reminder.R;

public class MyFacebookActivity extends BaseActivity {

    private Button share, send;
    private EditText shareText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_my_facebook);
        setTitle("Facebook");

        share = (Button) findViewById(R.id.share);
        send = (Button) findViewById(R.id.send);
        shareText = (EditText) findViewById(R.id.shareText);

        final ShareLinkContent shareLinkContent = new ShareLinkContent.Builder().build();

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", shareText.getText().toString());
                clipboard.setPrimaryClip(clip);
                ShareDialog.show(MyFacebookActivity.this, shareLinkContent);
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageDialog.show(MyFacebookActivity.this, shareLinkContent);
            }
        });
    }
}
