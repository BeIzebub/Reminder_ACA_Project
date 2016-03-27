package com.reminder.mobile_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.reminder.BaseActivity;
import com.reminder.CustomAdapterForSMS;
import com.reminder.DAO.RemindersDB;
import com.reminder.DAO.objects.SMSReminder;
import com.reminder.R;
import com.reminder.SwipeDismissListViewTouchListener;

import java.util.List;

public class AllSMSRemindersActivity extends BaseActivity {
    private List<SMSReminder> rems;
    private ListView listView;
    private RemindersDB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_smsreminders);

        setTitle("SMS reminders");

        listView = (ListView) findViewById(R.id.allSMS);
        Button add = (Button) findViewById(R.id.addSMS);
        db = RemindersDB.getInstance(this);
        rems = db.getAllSmsReminders();
        final CustomAdapterForSMS adapter = new CustomAdapterForSMS(this, rems);
        listView.setAdapter(adapter);
        //*************************************
        SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(listView,
                new SwipeDismissListViewTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }

                    @Override
                    public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions) {
                            int id = rems.get(position).getId();
                            rems.remove(position);
                            db.deleteSmsReminder(id);
                            db.deleteReminder(id);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
        listView.setOnTouchListener(touchListener);
        //***********************************
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AllSMSRemindersActivity.this, SmsActivity.class), 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        RemindersDB.getInstance(this).addSmsReminder((SMSReminder) data.getSerializableExtra("r"));
        List<SMSReminder> rems = RemindersDB.getInstance(this).getAllSmsReminders();
        CustomAdapterForSMS adapter = new CustomAdapterForSMS(this, rems);
        listView.setAdapter(adapter);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
