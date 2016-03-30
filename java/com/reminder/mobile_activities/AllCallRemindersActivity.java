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
import com.reminder.CustomAdapterForCalls;
import com.reminder.DAO.RemindersDB;
import com.reminder.DAO.objects.CallReminder;
import com.reminder.R;

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
        Button add = (Button) findViewById(R.id.addCall);
        db = RemindersDB.getInstance(this);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AllCallRemindersActivity.this, CallActivity.class), 0);
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
                db.deleteCallReminder(rems.get(position).getId());
                init();
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
    }

    private void init() {
        rems = db.getAllCallReminders();
        adapter = new CustomAdapterForCalls(this, rems);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            RemindersDB.getInstance(this).addCallReminder((CallReminder) data.getSerializableExtra("r"));
            List<CallReminder> rems = RemindersDB.getInstance(this).getAllCallReminders();
            CustomAdapterForCalls adapter = new CustomAdapterForCalls(this, rems);
            listView.setAdapter(adapter);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
