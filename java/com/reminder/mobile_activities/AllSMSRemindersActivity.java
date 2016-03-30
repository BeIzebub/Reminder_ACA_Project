package com.reminder.mobile_activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
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

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
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
        if (data != null) {
            RemindersDB.getInstance(this).addSmsReminder((SMSReminder) data.getSerializableExtra("r"));
            List<SMSReminder> rems = RemindersDB.getInstance(this).getAllSmsReminders();
            CustomAdapterForSMS adapter = new CustomAdapterForSMS(this, rems);
            listView.setAdapter(adapter);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
