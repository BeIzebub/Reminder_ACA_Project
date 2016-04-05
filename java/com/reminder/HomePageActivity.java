package com.reminder;

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
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.reminder.DAO.RemindersDB;
import com.reminder.DAO.objects.Reminder;
import com.reminder.DAO.objects.SMSReminder;
import com.reminder.mobile_activities.AllCallRemindersActivity;
import com.reminder.mobile_activities.AllSMSRemindersActivity;
import com.reminder.mobile_activities.services.CallReceiver;
import com.reminder.mobile_activities.services.SMSReceiver;
import com.reminder.mobile_activities.services.SimpleReceiver;
import com.reminder.other_activities.AllSimpleRemindersActivity;
import com.reminder.social_activities.AllFacebookReminders;
import com.reminder.social_activities.AllViberReminders;
import com.reminder.social_activities.social_service.FacebookReceiver;
import com.reminder.social_activities.social_service.ViberReceiver;

import java.util.List;

public class HomePageActivity extends BaseActivity {

    private List<Reminder> reminders;
    private SwipeMenuListView listView;
    private CustomAdapter adapter;
    private RemindersDB db;
    private Button newRem, newCall, newSms, newFace, newVibe;
    private TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        setTitle("Home");
        /***    ***/
        db = RemindersDB.getInstance(this);
        listView = (SwipeMenuListView) findViewById(R.id.allReminders);
        welcome = (TextView) findViewById(R.id.welcome);
        newRem = (Button) findViewById(R.id.newRem);
        newCall = (Button) findViewById(R.id.newCallRem);
        newSms = (Button) findViewById(R.id.newSmsRem);
        newFace = (Button) findViewById(R.id.newFace);
        newVibe = (Button) findViewById(R.id.newViber);
        init();

        if (!reminders.isEmpty()) {
            listView.setVisibility(View.VISIBLE);
            welcome.setVisibility(View.GONE);
            newRem.setVisibility(View.GONE);
            newCall.setVisibility(View.GONE);
            newSms.setVisibility(View.GONE);
            newFace.setVisibility(View.GONE);
            newVibe.setVisibility(View.GONE);
        }

        newRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, AllSimpleRemindersActivity.class));
            }
        });

        newCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, AllCallRemindersActivity.class));
            }
        });

        newSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, AllSMSRemindersActivity.class));
            }
        });

        newFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, AllFacebookReminders.class));
            }
        });

        newVibe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, AllViberReminders.class));
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
                switch (reminders.get(position).getType()) {
                    case Reminder.SIMPLE:
                        Intent myIntent = new Intent(HomePageActivity.this, SimpleReceiver.class);
                        boolean alarmUp = (PendingIntent.getBroadcast(HomePageActivity.this, reminders.get(position).getId(),
                                new Intent(HomePageActivity.this, SimpleReceiver.class),
                                PendingIntent.FLAG_ONE_SHOT) != null);
                        if (alarmUp)
                        {
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), reminders.get(position).getId(), myIntent,PendingIntent.FLAG_ONE_SHOT);
                            AlarmManager alarmManager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);
                            alarmManager.cancel(pendingIntent);
                        }
                    case Reminder.CALL_REMINDER:
                        myIntent = new Intent(HomePageActivity.this, CallReceiver.class);
                        alarmUp = (PendingIntent.getBroadcast(HomePageActivity.this, reminders.get(position).getId(),
                                new Intent(HomePageActivity.this, CallReceiver.class),
                                PendingIntent.FLAG_ONE_SHOT) != null);
                        if (alarmUp)
                        {
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), reminders.get(position).getId(), myIntent,PendingIntent.FLAG_ONE_SHOT);
                            AlarmManager alarmManager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);
                            alarmManager.cancel(pendingIntent);
                        }
                    case Reminder.SMS_REMINDER:
                        myIntent = new Intent(HomePageActivity.this, SMSReceiver.class);
                        alarmUp = (PendingIntent.getBroadcast(HomePageActivity.this, reminders.get(position).getId(),
                                new Intent(HomePageActivity.this, SMSReceiver.class),
                                PendingIntent.FLAG_ONE_SHOT) != null);
                        if (alarmUp)
                        {
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), reminders.get(position).getId(), myIntent,PendingIntent.FLAG_ONE_SHOT);
                            AlarmManager alarmManager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);
                            alarmManager.cancel(pendingIntent);
                        }
                    case Reminder.FACEBOOK_REMINDER:
                        myIntent = new Intent(HomePageActivity.this, FacebookReceiver.class);
                        alarmUp = (PendingIntent.getBroadcast(HomePageActivity.this, reminders.get(position).getId(),
                                new Intent(HomePageActivity.this, FacebookReceiver.class),
                                PendingIntent.FLAG_ONE_SHOT) != null);
                        if (alarmUp)
                        {
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), reminders.get(position).getId(), myIntent,PendingIntent.FLAG_ONE_SHOT);
                            AlarmManager alarmManager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);
                            alarmManager.cancel(pendingIntent);
                        }
                    case Reminder.VIBER_REMINDER:
                        myIntent = new Intent(HomePageActivity.this, ViberReceiver.class);
                        alarmUp = (PendingIntent.getBroadcast(HomePageActivity.this, reminders.get(position).getId(),
                                new Intent(HomePageActivity.this, ViberReceiver.class),
                                PendingIntent.FLAG_ONE_SHOT) != null);
                        if (alarmUp)
                        {
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), reminders.get(position).getId(), myIntent,PendingIntent.FLAG_ONE_SHOT);
                            AlarmManager alarmManager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);
                            alarmManager.cancel(pendingIntent);
                        }

                }
                db.deleteCallReminder(reminders.get(position).getId());
                init();
                adapter.notifyDataSetChanged();
                final Snackbar snackbar = Snackbar.make(listView, "Reminder deleted", Snackbar.LENGTH_LONG);
                snackbar.show();
                return false;
            }
        });

        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
    }

    private void init() {
        reminders = db.getAllReminders();
        adapter = new CustomAdapter(this, reminders);
        listView.setAdapter(adapter);
        if (reminders.isEmpty()) {
            listView.setVisibility(View.GONE);
            welcome.setVisibility(View.VISIBLE);
            newRem.setVisibility(View.VISIBLE);
            newCall.setVisibility(View.VISIBLE);
            newSms.setVisibility(View.VISIBLE);
            newFace.setVisibility(View.VISIBLE);
            newVibe.setVisibility(View.VISIBLE);
        }
    }
}
