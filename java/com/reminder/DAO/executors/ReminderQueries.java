package com.reminder.DAO.executors;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.reminder.DAO.DBHelper;
import com.reminder.DAO.executors.functionality.ReminderFunctionality;
import com.reminder.DAO.objects.Reminder;
import com.reminder.DAO.tables.ReminderTable;

/**
 * Created by Armen on 21.03.2016.
 */
public class ReminderQueries implements ReminderFunctionality{
    private String[] allColumns = {
            ReminderTable.COLUMN_ID,
            ReminderTable.COLUMN_COMMENT,
            ReminderTable.COLUMN_TIME
    };

    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public ReminderQueries(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public int addReminder(Reminder r) {
        ContentValues values = new ContentValues();
        values.put(ReminderTable.COLUMN_COMMENT, r.getComment());
        values.put(ReminderTable.COLUMN_TIME, r.getTimeInMillis());
        int id = (int) database.insert(ReminderTable.TABLE_NAME, null, values);
        Cursor cursor = database.query(ReminderTable.TABLE_NAME, allColumns, ReminderTable.COLUMN_ID + " = " + id, null, null, null, null);
        cursor.moveToFirst();
        int res = cursor.getInt(0);
        cursor.close();
        return res;
    }

    @Override
    public void editReminder(Reminder r) {
        ContentValues values = new ContentValues();
        values.put(ReminderTable.COLUMN_COMMENT, r.getComment());
        values.put(ReminderTable.COLUMN_TIME, r.getTimeInMillis());
        database.update(ReminderTable.TABLE_NAME, values, ReminderTable.COLUMN_ID + " = " + r.getId(), null);
    }

    @Override
    public void deleteReminder(int id) {
        database.delete(ReminderTable.TABLE_NAME, ReminderTable.COLUMN_ID + " = " + id, null);
    }

}
/*
reminder table
id comment date
1   asd    ....
2   ba     ....

calls table
id receiver
1   ....

sms table
id receiver text
2   ....    ....

add aneluc demic arec reminderneri mej heto et id-ov calli kam smsi
 */
