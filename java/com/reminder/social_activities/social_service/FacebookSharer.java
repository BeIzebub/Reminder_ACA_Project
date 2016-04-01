package com.reminder.social_activities.social_service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.reminder.social_activities.MyFacebookActivity;

/**
 * Created by Karen on 25-Mar-16.
 */
public class FacebookSharer extends Activity {

    private static final String TAG = "FbService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String action = (String) getIntent().getExtras().get("do_action");
        if (action.equals("sh")) {
            Intent i = getIntent();
            String text = (String) i.getExtras().get("text");
            Log.e(TAG, text);

            MyFacebookActivity.onFbLogin(FacebookSharer.this);

            final ShareLinkContent content = new ShareLinkContent.Builder().build();

            ShareApi.share(text , content, new FacebookCallback<Sharer.Result>() {

                @Override
                public void onSuccess(Sharer.Result result) {
                    Log.e(TAG, "SUCCESS");
                }

                @Override
                public void onCancel() {
                    Log.e(TAG, "CANCELLED");
                }

                @Override
                public void onError(FacebookException error) {
                    Log.e(TAG, error.toString());
                }
            });
        }
        finish();
    }
}
