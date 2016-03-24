package com.reminder.social_activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;

import com.reminder.BaseActivity;
import com.reminder.R;
import com.reminder.social_activities.social_service.MailReceiver;

public class MailActivity extends BaseActivity {

    private EditText to;
    private EditText subject;
    private EditText text;
    private EditText file;
    private Button send;
    private TextView time;
    private TextView date;
    private Calendar c;
    private Calendar selected;
    private int selectedDay, selectedMonth, selectedYear, selectedHour, selectedMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mail_activity);
        setTitle("Mail");
        to = (EditText) findViewById(R.id.toEdit);
        subject = (EditText) findViewById(R.id.subjectEdit);
        text = (EditText) findViewById(R.id.textEdit);
        send = (Button)findViewById(R.id.sendButton);
        date = (TextView)findViewById(R.id.dateText);
        time = (TextView)findViewById(R.id.timeText);
        selected = Calendar.getInstance();
        selectedDay = selected.get(Calendar.DAY_OF_MONTH);
        selectedMonth = selected.get(Calendar.MONTH) + 1;
        selectedYear = selected.get(Calendar.YEAR);
        selectedHour = selected.get(Calendar.HOUR_OF_DAY);
        selectedMinute = selected.get(Calendar.MINUTE);
        date.setText(selectedDay + "/" + selectedMonth + "/" + selectedYear);
        time.setText(selectedHour + ":" + selectedMinute);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog d = new DatePickerDialog(MailActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        selectedDay = dayOfMonth;
                        selectedMonth = monthOfYear + 1;
                        selectedYear = year;
                        date.setText(selectedDay + "/" + selectedMonth + "/" + selectedYear);
                        selected.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        selected.set(Calendar.MONTH, monthOfYear);
                        selected.set(Calendar.YEAR, year);
                    }
                }, selectedYear, selectedMonth - 1, selectedDay);
                d.show();
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog d = new TimePickerDialog(MailActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        selectedHour = hourOfDay;
                        selectedMinute = minute;
                        time.setText(selectedHour + ":" + selectedMinute);
                        selected.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selected.set(Calendar.MINUTE, minute);
                    }
                }, selectedHour, selectedMinute, true);
                d.show();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                if (selected.getTimeInMillis() < c.getTimeInMillis()) {
                    Toast.makeText(MailActivity.this, "Time must be future", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(MailActivity.this, MailReceiver.class);
                    startService(intent);
                    Toast.makeText(getApplicationContext(), "GMail Scheduled", Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(getApplicationContext(), MailReceiver.class);
                    myIntent.putExtra("to", to.getText().toString());
                    myIntent.putExtra("subject", subject.getText().toString());
                    myIntent.putExtra("text", text.getText().toString());

                    int id = (int) (System.currentTimeMillis() / 1000);
                    Calendar c = Calendar.getInstance();
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, myIntent, PendingIntent.FLAG_ONE_SHOT);
                    AlarmManager alarmManager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, selected.getTimeInMillis(), pendingIntent);
                }
            }
        });
    }
}
