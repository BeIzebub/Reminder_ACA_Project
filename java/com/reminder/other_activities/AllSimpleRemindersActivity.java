package com.reminder.other_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.reminder.BaseActivity;
import com.reminder.CustomAdapter;
import com.reminder.DAO.RemindersDB;
import com.reminder.DAO.objects.Reminder;
import com.reminder.R;

import java.util.List;

public class AllSimpleRemindersActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_simple_reminders);
        setTitle("Simple reminders");

        ListView listView = (ListView) findViewById(R.id.simpleRems);
        Button add = (Button) findViewById(R.id.addSimpleRem);

        List<Reminder> rems = RemindersDB.getInstance(this).getAllSimpleReminders();
        CustomAdapter adapter = new CustomAdapter(this, rems);
        listView.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllSimpleRemindersActivity.this, SimpleReminderActivity.class));
            }
        });
    }
}
