package com.reminder.DAO.tables;

/**
 * Created by Armen on 21.03.2016.
 */
public class ReminderTable {
    public static final String TABLE_NAME = "reminders";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_COMMENT = "comment";
    public static final String COLUMN_TIME = "time";

    public static final String CREATE_TABLE = "create table " + TABLE_NAME +
            "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_COMMENT + " text not null, "
            + COLUMN_TIME + " integer not null" + ");";
}
