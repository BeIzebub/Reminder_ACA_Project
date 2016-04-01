package com.reminder.mobile_activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.reminder.BaseActivity;
import com.reminder.DAO.RemindersDB;
import com.reminder.DAO.objects.CallReminder;
import com.reminder.R;
import com.reminder.mobile_activities.services.CallReceiver;

import java.util.Calendar;

public class CallActivity extends BaseActivity {

    private static final int PICK_CONTACT_CALL = 1001;
    private static final int RESULT_OK_CALL = -1;

    private Uri uriContact;
    private String contactID;

    private ImageView search;
    private EditText comment, phone;
    private TextView date, time;
    private Calendar selectedCalendar, currentCalendar;
    private int selectedDay, selectedMonth, selectedYear, selectedHour, selectedMinute;
    private RemindersDB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        setTitle("Call");
        db = RemindersDB.getInstance(this);
        Button call = (Button) findViewById(R.id.callBtn);
        phone = (EditText) findViewById(R.id.number);
        search = (ImageView) findViewById(R.id.imageButton);
        date = (TextView) findViewById(R.id.callDate);
        time = (TextView) findViewById(R.id.callTime);
        comment = (EditText) findViewById(R.id.editText3);
        selectedCalendar = Calendar.getInstance();
        currentCalendar = Calendar.getInstance();

        selectedDay = selectedCalendar.get(Calendar.DAY_OF_MONTH);
        selectedMonth = selectedCalendar.get(Calendar.MONTH) + 1;
        selectedYear = selectedCalendar.get(Calendar.YEAR);
        selectedHour = selectedCalendar.get(Calendar.HOUR_OF_DAY);
        selectedMinute = selectedCalendar.get(Calendar.MINUTE);

        date.setText(selectedDay + "/" + selectedMonth + "/" + selectedYear);
        time.setText(selectedHour + ":" + String.format("%02d\n", selectedMinute));

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(i, PICK_CONTACT_CALL);
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedCalendar.getTimeInMillis() < System.currentTimeMillis())
                    Toast.makeText(CallActivity.this, "Time must be future", Toast.LENGTH_SHORT).show();
                else {
                    boolean normal = true;
                    if (phone.getText().toString().equals("")) {
                        phone.setError("Input receiver's number");
                        normal = false;
                    }
                    if (normal) {
                    //    run();
                        CallReminder r = new CallReminder(comment.getText().toString(), selectedCalendar.getTimeInMillis(),  phone.getText().toString());
                        Intent i = new Intent();
                        i.putExtra("r", r);
                        setResult(0, i);
                        finish();
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
                        time.setText(selectedHour + ":" + String.format("%02d\n", selectedMinute));
                        selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedCalendar.set(Calendar.MINUTE, minute);
                    }
                }, selectedHour, selectedMinute, true);
                d.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null){
            uriContact = data.getData();
            if (resultCode == RESULT_OK_CALL) {
                switch (requestCode) {
                    case PICK_CONTACT_CALL:
                        if (resultCode == RESULT_OK_CALL) {
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
                                phone.setText(contactNumber);
                            }
                            cursorPhone.close();

                        }
                        break;
                }
            }}
        super.onActivityResult(requestCode, resultCode, data);
    }
}
