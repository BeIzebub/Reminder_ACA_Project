package com.reminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.reminder.DAO.objects.Reminder;
import com.reminder.DAO.objects.SMSReminder;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Gor on 3/25/2016.
 */
public class CustomAdapterForSMS extends BaseAdapter {
    private List<SMSReminder> reminders;
    private static LayoutInflater inflater = null;

    public CustomAdapterForSMS(Context activity, List<SMSReminder> reminders) {
        this.reminders = reminders;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return reminders.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        ImageView image;
        TextView title, date, time;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder h = new Holder();
        View row;
        row = inflater.inflate(R.layout.list_item, null);
        h.image = (ImageView) row.findViewById(R.id.remImage);
        h.title = (TextView) row.findViewById(R.id.title);
        h.date = (TextView) row.findViewById(R.id.date);
        h.time = (TextView) row.findViewById(R.id.time);

        Reminder reminder = reminders.get(position);
        Calendar c = Calendar.getInstance();

        h.image.setImageResource(R.drawable.sms);
        h.title.setText(reminder.getName());
        c.setTimeInMillis(reminder.getTimeInMillis());
        h.date.setText(c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
        h.time.setText(c.get(Calendar.HOUR_OF_DAY) + ":" + String.format("%02d\n", c.get(Calendar.MINUTE)));
        return row;
    }
}
