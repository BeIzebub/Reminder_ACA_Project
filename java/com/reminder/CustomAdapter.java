package com.reminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.reminder.DAO.objects.Reminder;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Armen on 22.03.2016.
 */
public class CustomAdapter extends BaseAdapter {
    private List<Reminder> reminders;
    private static LayoutInflater inflater = null;

    public CustomAdapter(Context activity, List<Reminder> reminders) {
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

        switch (reminder.getType()) {
            case Reminder.SIMPLE:
                h.image.setImageResource(R.drawable.simple_rem);
                break;
            case Reminder.CALL_REMINDER:
                h.image.setImageResource(R.drawable.call);
                break;
            case Reminder.SMS_REMINDER:
                h.image.setImageResource(R.drawable.sms);
                break;
            case Reminder.FACEBOOK_REMINDER:
                h.image.setImageResource(R.drawable.fb);
                break;        }
        h.title.setText(reminder.getName());
        c.setTimeInMillis(reminder.getTimeInMillis());
        h.date.setText(c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
        h.time.setText(c.get(Calendar.HOUR_OF_DAY) + ":" + String.format("%02d\n", c.get(Calendar.MINUTE)));
        return row;
    }
}
