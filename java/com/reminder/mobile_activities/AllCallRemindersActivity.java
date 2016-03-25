package com.reminder.mobile_activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.reminder.BaseActivity;
import com.reminder.CustomAdapter;
import com.reminder.CustomAdapterForCalls;
import com.reminder.DAO.RemindersDB;
import com.reminder.DAO.objects.CallReminder;
import com.reminder.DAO.objects.Reminder;
import com.reminder.R;

import java.util.List;

public class AllCallRemindersActivity extends BaseActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_call_reminders);
        setTitle("Call reminders");

        listView = (ListView) findViewById(R.id.allCalls);
        Button add = (Button) findViewById(R.id.addCall);

        List<CallReminder> rems = RemindersDB.getInstance(this).getAllCallReminders();
        CustomAdapterForCalls adapter = new CustomAdapterForCalls(this, rems);
        listView.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AllCallRemindersActivity.this, CallActivity.class), 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        RemindersDB.getInstance(this).addCallReminder((CallReminder)data.getSerializableExtra("r"));
        List<CallReminder> rems = RemindersDB.getInstance(this).getAllCallReminders();
        CustomAdapterForCalls adapter = new CustomAdapterForCalls(this, rems);
        listView.setAdapter(adapter);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
