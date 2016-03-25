package com.reminder.social_activities.social_service;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

/**
 * Created by Karen on 25-Mar-16.
 */
public class FacebookSharer extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        String action = (String) getIntent().getExtras().get("do_action");
        if (action.equals("sh")) {
            Intent i = getIntent();
            String text = (String) i.getExtras().get("text");
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", text);
            clipboard.setPrimaryClip(clip);

            final ShareLinkContent shareLinkContent = new ShareLinkContent.Builder().build();
            ShareDialog.show(FacebookSharer.this, shareLinkContent);
        }
        finish();
    }
}
