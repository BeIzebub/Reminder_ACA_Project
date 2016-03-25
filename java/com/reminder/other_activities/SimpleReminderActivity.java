package com.reminder.other_activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.reminder.BaseActivity;
import com.reminder.DAO.RemindersDB;
import com.reminder.DAO.objects.CallReminder;
import com.reminder.DAO.objects.Reminder;
import com.reminder.R;

import java.util.Calendar;


/**
 * Created by Armen on 24.03.2016.
 */
public class SimpleReminderActivity extends BaseActivity {

    private EditText name, comment;
    private TextView date, time;
    private Calendar selectedCalendar, currentCalendar;
    private int selectedDay, selectedMonth, selectedYear, selectedHour, selectedMinute;
    private RemindersDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_reminders);
        setTitle("Simple reminder");
        db = RemindersDB.getInstance(this);
        Button done = (Button) findViewById(R.id.button2);
        name = (EditText) findViewById(R.id.simple_reminder_name);
        date = (TextView) findViewById(R.id.reminderDate);
        time = (TextView) findViewById(R.id.reminderTime);
        comment = (EditText) findViewById(R.id.simple_reminder_comment);
        selectedCalendar = Calendar.getInstance();
        currentCalendar = Calendar.getInstance();

        selectedDay = selectedCalendar.get(Calendar.DAY_OF_MONTH);
        selectedMonth = selectedCalendar.get(Calendar.MONTH) + 1;
        selectedYear = selectedCalendar.get(Calendar.YEAR);
        selectedHour = selectedCalendar.get(Calendar.HOUR_OF_DAY);
        selectedMinute = selectedCalendar.get(Calendar.MINUTE);

        date.setText(selectedDay + "/" + selectedMonth + "/" + selectedYear);
        time.setText(selectedHour + ":" + String.format("%02d\n", selectedMinute));

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedCalendar.getTimeInMillis() < currentCalendar.getTimeInMillis())
                    Toast.makeText(SimpleReminderActivity.this, "Time must be future", Toast.LENGTH_SHORT).show();
                else {
                    boolean normal = true;
                    if (name.getText().toString().equals("")) {
                        name.setError("Input name");
                        normal = false;
                    }
                    if (normal) {
                        //   run();
                        Reminder r = new Reminder(name.getText().toString(), comment.getText().toString(), selectedCalendar.getTimeInMillis(), Reminder.SIMPLE);
                        db.addReminder(r);
                        Toast.makeText(SimpleReminderActivity.this, "Reminder saved", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog d = new DatePickerDialog(SimpleReminderActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        selectedDay = dayOfMonth;
                        selectedMonth = monthOfYear + 1;
                        selectedYear = year;
                        date.setText(selectedDay + "/" + selectedMonth + "/" + selectedYear);
                        selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        selectedCalendar.set(Calendar.MONTH, monthOfYear);
                        selectedCalendar.set(Calendar.YEAR, year);
                    }
                }, selectedYear, selectedMonth - 1, selectedDay);
                d.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog d = new TimePickerDialog(SimpleReminderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        selectedHour = hourOfDay;
                        selectedMinute = minute;
                        time.setText(selectedHour + ":" + String.format("%02d\n", selectedMinute));
                        selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedCalendar.set(Calendar.MINUTE, minute);
                    }
                }, selectedHour, selectedMinute, true);
                d.show();
            }
        });

    }
}
