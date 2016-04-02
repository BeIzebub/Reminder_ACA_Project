package com.reminder.other_activities;

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
import android.widget.ImageView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.reminder.BaseActivity;
import com.reminder.CustomAdapter;
import com.reminder.DAO.RemindersDB;
import com.reminder.DAO.objects.CallReminder;
import com.reminder.DAO.objects.Reminder;
import com.reminder.DAO.objects.SMSReminder;
import com.reminder.R;
import com.reminder.mobile_activities.services.CallReceiver;
import com.reminder.mobile_activities.services.SMSReceiver;
import com.reminder.mobile_activities.services.SimpleReceiver;

import java.util.List;

public class AllSimpleRemindersActivity extends BaseActivity {

    private SwipeMenuListView listView;
    private FloatingActionButton fab;
    private List<Reminder> rems;
    private CustomAdapter adapter;
    private RemindersDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_simple_reminders);
        setTitle("Simple reminders");

        listView = (SwipeMenuListView) findViewById(R.id.simpleRems);
        db = RemindersDB.getInstance(this);
        init();

        ImageView icon = new ImageView(this);
        icon.setImageResource(android.R.drawable.ic_input_add);
        FloatingActionButton fab = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AllSimpleRemindersActivity.this, SimpleReminderActivity.class), 0);
            }
        });

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
                final Reminder r = rems.get(position);

                Intent myIntent = new Intent(AllSimpleRemindersActivity.this, SimpleReceiver.class);
                PendingIntent pendingIntent;
                boolean alarmUp = (PendingIntent.getBroadcast(AllSimpleRemindersActivity.this, rems.get(position).getId(),
                        new Intent(AllSimpleRemindersActivity.this,SMSReceiver.class),
                        PendingIntent.FLAG_ONE_SHOT) != null);
                if (alarmUp)
                {
                    pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), rems.get(position).getId(), myIntent,PendingIntent.FLAG_ONE_SHOT);
                    AlarmManager alarmManager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);
                    alarmManager.cancel(pendingIntent);
                }
                db.deleteReminder(rems.get(position).getId());
                init();
                adapter.notifyDataSetChanged();
                final Snackbar snackbar = Snackbar.make(listView, "Reminder deleted", Snackbar.LENGTH_LONG);
                snackbar.setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.addReminder(r);
                        init();
                        run(r, r.getId());
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
                return false;
            }
        });

        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
    }

    private void init() {
        rems = db.getAllSimpleReminders();
        adapter = new CustomAdapter(this, rems);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Reminder r = (Reminder) data.getSerializableExtra("r");
            int id = db.addReminder(r);
            init();
            run(r, id);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void run(Reminder r, int id) {
        Intent myIntent = new Intent(this, SimpleReceiver.class);
        myIntent.putExtra("n", r.getName());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,  id, myIntent, PendingIntent.FLAG_ONE_SHOT);

        AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, r.getTimeInMillis(), pendingIntent);
    }
}
