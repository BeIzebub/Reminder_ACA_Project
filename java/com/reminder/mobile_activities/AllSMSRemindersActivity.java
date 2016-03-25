package com.reminder.mobile_activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.reminder.BaseActivity;
import com.reminder.CustomAdapterForCalls;
import com.reminder.CustomAdapterForSMS;
import com.reminder.DAO.RemindersDB;
import com.reminder.DAO.objects.CallReminder;
import com.reminder.DAO.objects.SMSReminder;
import com.reminder.R;

import java.util.List;

public class AllSMSRemindersActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_smsreminders);

        setTitle("SMS reminders");

        ListView listView = (ListView) findViewById(R.id.allSMS);
        Button add = (Button) findViewById(R.id.addSMS);

        List<SMSReminder> rems = RemindersDB.getInstance(this).getAllSmsReminders();
        CustomAdapterForSMS adapter = new CustomAdapterForSMS(this, rems);
        listView.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AllSMSRemindersActivity.this, SmsActivity.class), 0);
            }
        });
    }
}
