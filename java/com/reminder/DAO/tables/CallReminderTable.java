package com.reminder.DAO.tables;

/**
 * Created by Armen on 21.03.2016.
 */
public class CallReminderTable {
    public static final String TABLE_NAME = "calls";
    public static final String COLUMN_ID = "reminder_id";
    public static final String COLUMN_RECEIVER = "receiver";

    public static final String CREATE_TABLE = "create table " + TABLE_NAME +
            "(" + COLUMN_ID + " integer primary key, "
            + COLUMN_RECEIVER + " text not null, " + ");";
}
