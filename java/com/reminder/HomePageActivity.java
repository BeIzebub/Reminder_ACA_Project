package com.reminder;

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
import com.reminder.mobile_activities.AllCallRemindersActivity;
import com.reminder.mobile_activities.AllSMSRemindersActivity;
import com.reminder.other_activities.AllSimpleRemindersActivity;
import com.reminder.social_activities.AllFacebookReminders;

import java.util.List;

public class HomePageActivity extends BaseActivity {

    private List<Reminder> calls;
    private SwipeMenuListView listView;
    private CustomAdapter adapter;
    private RemindersDB db;
    private Button newRem, newCall, newSms, newFace;
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
        init();

        if (!calls.isEmpty()) {
            listView.setVisibility(View.VISIBLE);
            welcome.setVisibility(View.GONE);
            newRem.setVisibility(View.GONE);
            newCall.setVisibility(View.GONE);
            newSms.setVisibility(View.GONE);
            newFace.setVisibility(View.GONE);
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
                db.deleteCallReminder(calls.get(position).getId());
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
        calls = db.getAllReminders();
        adapter = new CustomAdapter(this, calls);
        listView.setAdapter(adapter);
        if (calls.isEmpty()) {
            listView.setVisibility(View.GONE);
            welcome.setVisibility(View.VISIBLE);
            newRem.setVisibility(View.VISIBLE);
            newCall.setVisibility(View.VISIBLE);
            newSms.setVisibility(View.VISIBLE);
            newFace.setVisibility(View.VISIBLE);
        }
    }
}
