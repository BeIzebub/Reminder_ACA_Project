package com.reminder;

import android.os.Bundle;
import android.widget.ListView;

import com.reminder.DAO.RemindersDB;
import com.reminder.DAO.objects.Reminder;

import java.util.List;

public class HomePageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        setTitle("Home");
        /***    ***/
        ListView listView = (ListView) findViewById(R.id.allReminders);
        List<Reminder> calls = RemindersDB.getInstance(this).getAllReminders();
        CustomAdapter adapter = new CustomAdapter(this, calls);
        listView.setAdapter(adapter);
    }
}
