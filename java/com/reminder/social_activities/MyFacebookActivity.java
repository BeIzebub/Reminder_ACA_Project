package com.reminder.social_activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.reminder.BaseActivity;
import com.reminder.DAO.RemindersDB;
import com.reminder.DAO.objects.Reminder;
import com.reminder.R;
import com.reminder.social_activities.social_service.FacebookReceiver;

import java.util.Arrays;
import java.util.Calendar;

public class MyFacebookActivity extends BaseActivity {

    private static final String TAG = "FB";
    public static CallbackManager callbackManager;

    private EditText text;
    private TextView time;
    private TextView date;
    private Calendar selected;
    private int selectedDay, selectedMonth, selectedYear, selectedHour, selectedMinute;

    private RemindersDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_my_facebook);

        setTitle("Facebook");

        db = RemindersDB.getInstance(this);

        text = (EditText) findViewById(R.id.text);
        date = (TextView) findViewById(R.id.dateText);
        time = (TextView) findViewById(R.id.timeText);

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

                DatePickerDialog d = new DatePickerDialog(MyFacebookActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                TimePickerDialog d = new TimePickerDialog(MyFacebookActivity.this, new TimePickerDialog.OnTimeSetListener() {
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

        onFbLogin(MyFacebookActivity.this);
    }

    public static void onFbLogin(Context context) {
        callbackManager = CallbackManager.Factory.create();

        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions((Activity) context, Arrays.asList("email", "user_photos", "public_profile"));

        LoginManager.getInstance().logInWithPublishPermissions((Activity) context, Arrays.asList("publish_actions"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        System.out.println("Success");
                    }
                    @Override
                    public void onCancel() {
                        Log.d(TAG,"On cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, error.toString());
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, R.menu.menu_for_sms, 0, "Schedule call")
                .setIcon(R.drawable.ic_done)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (selected.getTimeInMillis() < System.currentTimeMillis()) {
                            Toast.makeText(MyFacebookActivity.this, "Time must be future", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(MyFacebookActivity.this, FacebookReceiver.class);
                            startService(intent);
                            Toast.makeText(getApplicationContext(), "Facebook post scheduled", Toast.LENGTH_SHORT).show();
                            Intent myIntent = new Intent(getApplicationContext(), FacebookReceiver.class);
                            myIntent.putExtra("text", text.getText().toString());

                            Reminder r = new Reminder(text.getText().toString(), "", selected.getTimeInMillis(), Reminder.FACEBOOK_REMINDER);
                            int id = RemindersDB.getInstance(getApplicationContext()).addReminder(r);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, myIntent, PendingIntent.FLAG_ONE_SHOT);
                            AlarmManager alarmManager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, selected.getTimeInMillis(), pendingIntent);

                            Intent i = new Intent();
                            i.putExtra("r", r);
                            setResult(0, i);
                            finish();
                        }

                        return false;
                    }
                })
                .setShowAsAction(
                        MenuItem.SHOW_AS_ACTION_ALWAYS
                                | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return true;
    }
}
