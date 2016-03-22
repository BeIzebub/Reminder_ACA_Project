package com.reminder.DAO.executors;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.reminder.DAO.DBHelper;
import com.reminder.DAO.executors.functionality.SMSFunctionality;
import com.reminder.DAO.objects.Reminder;
import com.reminder.DAO.objects.SMSReminder;
import com.reminder.DAO.tables.SMSReminderTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Armen on 21.03.2016.
 */
public class SMSQueries implements SMSFunctionality{

    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private Context context;

    public SMSQueries(Context context) {
        dbHelper = new DBHelper(context);
        this.context = context;
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    @Override
    public void addSmsReminder(SMSReminder sms) {
        ReminderQueries rq = new ReminderQueries(context);
        rq.open();
        int id = rq.addReminder(sms);
        rq.close();
        ContentValues values = new ContentValues();
        values.put(SMSReminderTable.COLUMN_ID, id);
        values.put(SMSReminderTable.COLUMN_RECEIVER, sms.getReceiver());
        values.put(SMSReminderTable.COLUMN_TEXT, sms.getText());
        database.insert(SMSReminderTable.TABLE_NAME, null, values);
    }

    @Override
    public void editSmsReminder(SMSReminder sms) {
        ContentValues values = new ContentValues();
        values.put(SMSReminderTable.COLUMN_RECEIVER, sms.getReceiver());
        values.put(SMSReminderTable.COLUMN_TEXT, sms.getText());
        database.update(SMSReminderTable.TABLE_NAME, values, SMSReminderTable.COLUMN_ID + " = " + sms.getId(), null);
    }

    @Override
    public void deleteSmsReminder(int id) {
        database.delete(SMSReminderTable.TABLE_NAME, SMSReminderTable.COLUMN_ID + " = " + id, null);
    }

    @Override
    public List<SMSReminder> getAllSmsReminders() {
        List<SMSReminder> reminders = new ArrayList<>();
        String query = "SELECT reminders.id, reminders.time, reminders.comment, sms.receiver, sms.text " +
                "FROM reminders " +
                "INNER JOIN sms " +
                "ON reminders.id = sms.reminder_id";

        Cursor cursor  = database.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            SMSReminder reminder = new SMSReminder(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            reminders.add(reminder);
            cursor.moveToNext();
        }
        cursor.close();
        return  reminders;
    }
}
