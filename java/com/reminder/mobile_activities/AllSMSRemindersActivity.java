package com.reminder.mobile_activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.reminder.BaseActivity;
import com.reminder.CustomAdapterForSMS;
import com.reminder.DAO.RemindersDB;
import com.reminder.DAO.objects.SMSReminder;
import com.reminder.R;
import com.reminder.mobile_activities.services.SMSReceiver;
import com.reminder.mobile_activities.services.SMSService;

import java.util.Collections;
import java.util.List;

public class AllSMSRemindersActivity extends BaseActivity {

    private List<SMSReminder> rems;
    private SwipeMenuListView listView;
    private RemindersDB db;
    private CustomAdapterForSMS adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_smsreminders);

        setTitle("SMS reminders");

        listView = (SwipeMenuListView) findViewById(R.id.allSMS);
        Button add = (Button) findViewById(R.id.addSMS);
        db = RemindersDB.getInstance(this);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AllSMSRemindersActivity.this, SmsActivity.class), 0);
            }
        });
        init();

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                Display display = getWindowManager().getDefaultDisplay();
                deleteItem.setWidth(display.getWidth()/2);
                deleteItem.setTitle("Delete this reminder?");
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(Color.WHITE);
                deleteItem.setIcon(android.R.drawable.ic_menu_delete);
                menu.addMenuItem(deleteItem);
            }
        };

        listView.setMenuCreator(creator);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(AllSMSRemindersActivity.this, SmsActivity.class);
                i.putExtra("sms", db.getAllSmsReminders().get(position));
                startActivity(i);
            }
        });
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Intent myIntent = new Intent(AllSMSRemindersActivity.this, SMSReceiver.class);
                PendingIntent pendingIntent;
                init();
                boolean alarmUp = (PendingIntent.getBroadcast(AllSMSRemindersActivity.this, rems.get(position).getId(),
                        new Intent(AllSMSRemindersActivity.this,SMSReceiver.class),
                        PendingIntent.FLAG_ONE_SHOT) != null);
                if (alarmUp)
                {
                    pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), rems.get(position).getId(), myIntent,PendingIntent.FLAG_ONE_SHOT);
                    AlarmManager alarmManager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);
                    alarmManager.cancel(pendingIntent);
                }
                db.deleteSmsReminder(rems.get(position).getId());
                init();
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
    }

    private void init() {
        rems = db.getAllSmsReminders();
        adapter = new CustomAdapterForSMS(this, rems);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            List<SMSReminder> rems = RemindersDB.getInstance(this).getAllSmsReminders();
            CustomAdapterForSMS adapter = new CustomAdapterForSMS(this, rems);
            listView.setAdapter(adapter);
            super.onActivityResult(requestCode, resultCode, data);
    }
}
