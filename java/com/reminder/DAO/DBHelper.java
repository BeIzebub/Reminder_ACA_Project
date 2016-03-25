package com.reminder.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.reminder.DAO.tables.CallReminderTable;
import com.reminder.DAO.tables.ReminderTable;
import com.reminder.DAO.tables.SMSReminderTable;

/**
 * Created by Armen on 21.03.2016.
 */
public class DBHelper  extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "reminders.db";
    public static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ReminderTable.CREATE_TABLE);
        db.execSQL(CallReminderTable.CREATE_TABLE);
        db.execSQL(SMSReminderTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + ReminderTable.TABLE_NAME);
        db.execSQL("drop table if exists " + CallReminderTable.TABLE_NAME);
        db.execSQL("drop table if exists " + SMSReminderTable.TABLE_NAME);
    }
}