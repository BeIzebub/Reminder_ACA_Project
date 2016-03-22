package com.reminder.DAO.executors;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.reminder.DAO.DBHelper;
import com.reminder.DAO.executors.functionality.CallFunctionality;
import com.reminder.DAO.objects.CallReminder;
import com.reminder.DAO.tables.CallReminderTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Armen on 21.03.2016.
 */
public class CallQueries implements CallFunctionality{

    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private Context context;

    public CallQueries(Context context) {
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
    public void addCallReminder(CallReminder r) {
        ReminderQueries rq = new ReminderQueries(context);
        rq.open();
        int id = rq.addReminder(r);
        rq.close();
        ContentValues values = new ContentValues();
        values.put(CallReminderTable.COLUMN_ID, id);
        values.put(CallReminderTable.COLUMN_RECEIVER, r.getReceiver());
        database.insert(CallReminderTable.TABLE_NAME, null, values);
    }

    @Override
    public void editCallReminder(CallReminder r) {
        ContentValues values = new ContentValues();
        values.put(CallReminderTable.COLUMN_RECEIVER, r.getReceiver());
        database.update(CallReminderTable.TABLE_NAME, values, CallReminderTable.COLUMN_ID + " = " + r.getId(), null);
    }

    @Override
    public void deleteCallReminder(int id) {
        database.delete(CallReminderTable.TABLE_NAME, CallReminderTable.COLUMN_ID + " = " + id, null);
    }

    @Override
    public List<CallReminder> getAllCallReminders() {
        List<CallReminder> reminders = new ArrayList<>();
        String query = "SELECT reminders.id, reminders.name, reminders.time, reminders.comment, calls.receiver " +
                "FROM reminders " +
                "INNER JOIN calls " +
                "ON reminders.id = calls.reminder_id ";

        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CallReminder reminder = new CallReminder(cursor.getInt(0),cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4));
            reminders.add(reminder);
            cursor.moveToNext();
        }
        cursor.close();
        return reminders;
    }
}
