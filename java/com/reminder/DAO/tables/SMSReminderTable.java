package com.reminder.DAO.tables;

/**
 * Created by Armen on 22.03.2016.
 */
public class SMSReminderTable {
    public static final String TABLE_NAME = "sms";
    public static final String COLUMN_ID = "reminder_id";
    public static final String COLUMN_RECEIVER = "receiver";
    public static final String COLUMN_TEXT = "text";

    public static final String CREATE_TABLE = "create table " + TABLE_NAME +
            "(" + COLUMN_ID + " integer primary key, "
            + COLUMN_RECEIVER + " text not null, "
            + COLUMN_TEXT + " text not null" + ");";
}
