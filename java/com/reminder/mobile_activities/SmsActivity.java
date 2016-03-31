package com.reminder.mobile_activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.reminder.BaseActivity;
import com.reminder.DAO.RemindersDB;
import com.reminder.DAO.objects.CallReminder;
import com.reminder.DAO.objects.SMSReminder;
import com.reminder.R;
import com.reminder.mobile_activities.services.SMSReceiver;

import java.util.Calendar;


public class SmsActivity extends BaseActivity {

    private TextView date;
    private TextView time;
    private EditText phoneEdit;
    private EditText textEdit;
    private EditText commentEdit;
    private Calendar newCalendar;
    private ImageView search;
    public String phone;
    public String text;
    private int selectedDay, selectedMonth, selectedYear, selectedHour, selectedMinute;

    private static final int PICK_CONTACT = 1001;
    private static final int RESULT_OK = -1;
    private Uri uriContact;
    private String contactID;


    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, R.menu.menu_for_sms, 0, "Schedule SMS")
                .setIcon(android.R.drawable.ic_menu_send)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        schedule();
                        return false;
                    }
                })
                .setShowAsAction(
                        MenuItem.SHOW_AS_ACTION_ALWAYS
                                | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        setTitle("SMS");
        date = (TextView) findViewById(R.id.date);
        time = (TextView) findViewById(R.id.time);
        phoneEdit = (EditText) findViewById(R.id.editText);
        textEdit = (EditText) findViewById(R.id.editText2);
        commentEdit = (EditText) findViewById(R.id.commentEdit);
        newCalendar = Calendar.getInstance();

        search = (ImageView) findViewById(R.id.search);
        selectedDay = newCalendar.get(Calendar.DAY_OF_MONTH);
        selectedMonth = newCalendar.get(Calendar.MONTH) + 1;
        selectedYear = newCalendar.get(Calendar.YEAR);
        selectedHour = newCalendar.get(Calendar.HOUR_OF_DAY);
        selectedMinute = newCalendar.get(Calendar.MINUTE);
        date.setText(selectedDay + "/" + selectedMonth + "/" + selectedYear);
        time.setText(selectedHour + ":" + selectedMinute);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });


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
                }, selectedYear, selectedMonth - 1, selectedDay);
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
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null){
        uriContact = data.getData();
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_CONTACT:
                    if (resultCode == RESULT_OK) {
                        String contactNumber;
                        Cursor cursorID = getContentResolver().query(uriContact,
                                new String[]{ContactsContract.Contacts._ID},
                                null, null, null);
                        if (cursorID.moveToFirst()) {
                            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
                        }
                        cursorID.close();
                        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                                new String[]{contactID},
                                null);
                        if (cursorPhone.moveToFirst()) {
                            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            phoneEdit.setText(contactNumber);
                        }
                        cursorPhone.close();

                    }
                    break;
            }
        }}
    }

    public void schedule() {
        Calendar c = Calendar.getInstance();
        if (phoneEdit.getText().toString().matches("")) {
            phoneEdit.setError("Enter the Phone");
        } else {
            if (textEdit.getText().toString().matches("")) {
                textEdit.setError("Enter the Text");
            } else {
                if (newCalendar.getTimeInMillis() < c.getTimeInMillis()) {
                    Toast.makeText(SmsActivity.this, "Time must be future", Toast.LENGTH_SHORT).show();
                } else {
                    SMSReminder r = new SMSReminder(commentEdit.getText().toString(), newCalendar.getTimeInMillis(), phoneEdit.getText().toString(), textEdit.getText().toString());
                    phone = phoneEdit.getText().toString();
                    text = textEdit.getText().toString();
                    Intent intent = new Intent(SmsActivity.this, SMSReceiver.class);
                    startService(intent);
                    Toast.makeText(getApplicationContext(), "SMS Scheduled", Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(getApplicationContext(), SMSReceiver.class);
                    myIntent.putExtra("phone", phone);
                    myIntent.putExtra("text", text);
                    if(! commentEdit.getText().toString().matches("")) {
                        myIntent.putExtra("comment", commentEdit.getText().toString());
                    }
                    int id = RemindersDB.getInstance(this).addSmsReminder(r);
                    PendingIntent pendingIntent;
                    pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, myIntent, PendingIntent.FLAG_ONE_SHOT);
                    AlarmManager alarmManager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, newCalendar.getTimeInMillis(), pendingIntent);
                    finish();
                }
            }
        }
    }

}


