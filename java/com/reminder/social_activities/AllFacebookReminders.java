package com.reminder.social_activities;

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
import com.reminder.DAO.objects.Reminder;
import com.reminder.R;

import java.util.List;

public class AllFacebookReminders extends BaseActivity {

    private List<Reminder> reminders;
    private SwipeMenuListView listView;
    private CustomAdapter adapter;
    private RemindersDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_facebook_reminders);
        db = RemindersDB.getInstance(this);
        listView = (SwipeMenuListView) findViewById(R.id.faceRems);
        init();

        ImageView icon = new ImageView(this);
        icon.setImageResource(android.R.drawable.ic_input_add);
        FloatingActionButton fab = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AllFacebookReminders.this, MyFacebookActivity.class), 0);
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
                deleteItem.setWidth(display.getWidth() / 2);
                deleteItem.setTitle("Delete this reminder?");
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(Color.WHITE);
                deleteItem.setIcon(android.R.drawable.ic_menu_delete);
                menu.addMenuItem(deleteItem);
            }
        };

        listView.setMenuCreator(creator);
        reminders = db.getAllFacebookReminders();

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                final Reminder r = reminders.get(position);
                db.deleteCallReminder(reminders.get(position).getId());
                init();
                adapter.notifyDataSetChanged();
                final Snackbar snackbar = Snackbar.make(listView, "Reminder deleted", Snackbar.LENGTH_LONG);
                snackbar.setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.addReminder(r);
                        init();
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
        reminders = db.getAllFacebookReminders();
        adapter = new CustomAdapter(this, reminders);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Reminder rem = (Reminder) data.getSerializableExtra("r");
            int id =  db.addReminder(rem);
            init();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
