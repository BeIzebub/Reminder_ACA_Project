package com.reminder.mobile_activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.reminder.BaseActivity;
import com.reminder.R;
import com.reminder.mobile_activities.services.SMSReceiver;

import java.util.Calendar;

public class SmsActivity extends BaseActivity {

    private TextView date;
    private TextView time;
    private Button start;
    private EditText edit;
    private EditText edit2;
    private Calendar newCalendar;

    public static String phone;
    public static String text;
    private int selectedDay, selectedMonth, selectedYear, selectedHour, selectedMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        setTitle("SMS");
        date = (TextView) findViewById(R.id.date);
        time = (TextView) findViewById(R.id.time);
        start = (Button) findViewById(R.id.start);
        edit = (EditText) findViewById(R.id.editText);
        edit2 = (EditText) findViewById(R.id.editText2);
        newCalendar = Calendar.getInstance();

        selectedDay = newCalendar.get(Calendar.DAY_OF_MONTH);
        selectedMonth = newCalendar.get(Calendar.MONTH) + 1;
        selectedYear = newCalendar.get(Calendar.YEAR);
        selectedHour = newCalendar.get(Calendar.HOUR_OF_DAY);
        selectedMinute = newCalendar.get(Calendar.MINUTE);
        date.setText(selectedDay + "/" + selectedMonth + "/" + selectedYear);
        time.setText(selectedHour + ":" + selectedMinute);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog d = new DatePickerDialog(SmsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        selectedDay = dayOfMonth;
                        selectedMonth = monthOfYear + 1;
                        selectedYear = year;
                        date.setText(selectedDay + "/" + selectedMonth + "/" + selectedYear);
                        newCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        newCalendar.set(Calendar.MONTH, monthOfYear);
                        newCalendar.set(Calendar.YEAR, year);
                    }
                },selectedYear, selectedMonth - 1, selectedDay);
                d.show();
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog d = new TimePickerDialog(SmsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        selectedHour = hourOfDay;
                        selectedMinute = minute;
                        time.setText(selectedHour + ":" + selectedMinute);
                        newCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        newCalendar.set(Calendar.MINUTE, minute);
                    }
                }, selectedHour, selectedMinute, true);
                d.show();
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = edit.getText().toString();
                text = edit2.getText().toString();
                Intent intent = new Intent(SmsActivity.this, SMSReceiver.class);
                startService(intent);
                Toast.makeText(getApplicationContext(), "SMS Scheduled", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(getApplicationContext(), SMSReceiver.class);
                myIntent.putExtra("phone", phone);
                myIntent.putExtra("text", text);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),  0, myIntent, 0);
                AlarmManager alarmManager = (AlarmManager)getApplication().getSystemService(Context.ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, newCalendar.getTimeInMillis(), pendingIntent);
            }
        });
    }
}