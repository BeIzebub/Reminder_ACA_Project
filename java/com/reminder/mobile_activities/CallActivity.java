package com.reminder.mobile_activities;

import android.os.Bundle;

import com.reminder.BaseActivity;
import com.reminder.R;

public class CallActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        setTitle("Call");
    }
}