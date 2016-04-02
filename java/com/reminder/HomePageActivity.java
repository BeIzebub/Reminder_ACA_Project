package com.reminder;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.reminder.DAO.RemindersDB;
import com.reminder.DAO.executors.CallQueries;
import com.reminder.DAO.objects.CallReminder;
import com.reminder.DAO.objects.Reminder;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends BaseActivity {

    private List<Reminder> calls;
    private SwipeMenuListView listView;
    private CustomAdapter adapter;
    private RemindersDB db;
    private List<CallReminder> allCalls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        setTitle("Home");
        /***    ***/
        db = RemindersDB.getInstance(this);
        listView = (SwipeMenuListView) findViewById(R.id.allReminders);
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
        allCalls = db.getAllCallReminders();

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                final Reminder r = calls.get(position);
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
    }
}
