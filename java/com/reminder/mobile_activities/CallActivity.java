package com.reminder.mobile_activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.reminder.BaseActivity;
import com.reminder.R;
import com.reminder.mobile_activities.services.CallReceiver;

import java.util.Calendar;

public class CallActivity extends BaseActivity {

    private EditText phone;
    private TextView date, time;
    private Calendar selectedCalendar, currentCalendar;
    private int selectedDay, selectedMonth, selectedYear, selectedHour, selectedMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        setTitle("Call");
        Button call = (Button) findViewById(R.id.callBtn);
        phone = (EditText) findViewById(R.id.number);
        date = (TextView) findViewById(R.id.callDate);
        time = (TextView) findViewById(R.id.callTime);
        selectedCalendar = Calendar.getInstance();
        currentCalendar = Calendar.getInstance();

        selectedDay = selectedCalendar.get(Calendar.DAY_OF_MONTH);
        selectedMonth = selectedCalendar.get(Calendar.MONTH) + 1;
        selectedYear = selectedCalendar.get(Calendar.YEAR);
        selectedHour = selectedCalendar.get(Calendar.HOUR_OF_DAY);
        selectedMinute = selectedCalendar.get(Calendar.MINUTE);

        date.setText(selectedDay + "/" + selectedMonth + "/" + selectedYear);
        time.setText(selectedHour + ":" + selectedMinute);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedCalendar.getTimeInMillis() < currentCalendar.getTimeInMillis())
                    Toast.makeText(CallActivity.this, "Time must be future", Toast.LENGTH_SHORT).show();
                else {
                    if (phone.getText().toString().equals(""))
                        phone.setError("Input receiver's number");
                    else {
                        run();
                        Toast.makeText(CallActivity.this, "Call scheduled", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog d = new DatePickerDialog(CallActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                TimePickerDialog d = new TimePickerDialog(CallActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        selectedHour = hourOfDay;
                        selectedMinute = minute;
                        time.setText(selectedHour + ":" + selectedMinute);
                        selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedCalendar.set(Calendar.MINUTE, minute);
                    }
                }, selectedHour, selectedMinute, true);
                d.show();
            }
        });
    }

    private void run() {
        Intent myIntent = new Intent(this, CallReceiver.class);
        myIntent.putExtra("n", phone.getText().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,  0, myIntent, 0);

        AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, selectedCalendar.getTimeInMillis(), pendingIntent);
    }
}
