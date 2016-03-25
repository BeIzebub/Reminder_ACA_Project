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

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_simple_reminders);
        setTitle("Simple reminders");

        listView = (ListView) findViewById(R.id.simpleRems);
        Button add = (Button) findViewById(R.id.addSimpleRem);

        List<Reminder> rems = RemindersDB.getInstance(this).getAllSimpleReminders();
        CustomAdapter adapter = new CustomAdapter(this, rems);
        listView.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AllSimpleRemindersActivity.this, SimpleReminderActivity.class), 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            RemindersDB.getInstance(this).addReminder((Reminder) data.getSerializableExtra("r"));
            List<Reminder> rems = RemindersDB.getInstance(this).getAllSimpleReminders();
            CustomAdapter adapter = new CustomAdapter(this, rems);
            listView.setAdapter(adapter);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
