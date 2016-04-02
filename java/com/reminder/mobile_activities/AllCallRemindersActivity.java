package com.reminder.mobile_activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.reminder.BaseActivity;
import com.reminder.CustomAdapterForCalls;
import com.reminder.DAO.RemindersDB;
import com.reminder.DAO.objects.CallReminder;
import com.reminder.DAO.objects.Reminder;
import com.reminder.R;
import com.reminder.mobile_activities.services.CallReceiver;
import com.reminder.mobile_activities.services.SMSReceiver;
import com.reminder.other_activities.SimpleReminderActivity;

import java.util.Collections;
import java.util.List;

public class AllCallRemindersActivity extends BaseActivity {

    private SwipeMenuListView listView;
    private List<CallReminder> rems;
    private CustomAdapterForCalls adapter;
    private RemindersDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_call_reminders);
        setTitle("Call reminders");

        listView = (SwipeMenuListView) findViewById(R.id.allCalls);
        db = RemindersDB.getInstance(this);
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

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                final CallReminder r = rems.get(position);

                Intent myIntent = new Intent(AllCallRemindersActivity.this, CallReceiver.class);
                PendingIntent pendingIntent;
                boolean alarmUp = (PendingIntent.getBroadcast(AllCallRemindersActivity.this, rems.get(position).getId(),
                        new Intent(AllCallRemindersActivity.this,CallReceiver.class),
                        PendingIntent.FLAG_ONE_SHOT) != null);
                if (alarmUp) {
                    pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), rems.get(position).getId(), myIntent,PendingIntent.FLAG_ONE_SHOT);
                    AlarmManager alarmManager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);
                    alarmManager.cancel(pendingIntent);
                }

                db.deleteCallReminder(rems.get(position).getId());
                init();
                adapter.notifyDataSetChanged();
                final Snackbar snackbar = Snackbar.make(listView, "Reminder deleted", Snackbar.LENGTH_LONG);
                snackbar.setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.addCallReminder(r);
                        run(r, r.getId());
                        init();
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
                return false;
            }
        });

        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        ImageView icon = new ImageView(this);
        icon.setImageResource(android.R.drawable.ic_input_add);
        FloatingActionButton fab = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AllCallRemindersActivity.this, CallActivity.class), 0);
            }
        });
    }

    private void init() {
        rems = db.getAllCallReminders();
        adapter = new CustomAdapterForCalls(this, rems);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            CallReminder call = (CallReminder) data.getSerializableExtra("r");
            int id =  db.addCallReminder(call);
            init();
            run(call, id);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void run(CallReminder r, int id) {
        Intent myIntent = new Intent(this, CallReceiver.class);
        myIntent.putExtra("n", r.getReceiver());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,  id, myIntent, PendingIntent.FLAG_ONE_SHOT);

        AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, r.getTimeInMillis(), pendingIntent);
    }
}
