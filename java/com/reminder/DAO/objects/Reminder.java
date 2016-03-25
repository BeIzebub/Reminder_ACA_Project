package com.reminder.DAO.objects;


import java.io.Serializable;

/**
 * Created by Armen on 21.03.2016.
 */
public class Reminder implements Serializable{

    public static final int SIMPLE = 0;
    public static final int CALL_REMINDER = 1;
    public static final int SMS_REMINDER = 2;
    public static final int GMAIL_REMINDER = 3;
    public static final int FACEBOOK_REMINDER = 4;
    public static final int TWITTER_REMINDER = 5;

    protected int id;
    protected String name;
    protected long timeInMillis;
    protected String comment;
    protected int type = 0;

    public Reminder() {

    }

    public Reminder(String name, String comment, long time, int type) {
        this.name = name;
        this.timeInMillis = time;
        this.comment = comment;
        this.type = type;
    }

    public Reminder(int id,String name, String comment, long time, int type) {
        this.id = id;
        this.name = name;
        this.timeInMillis = time;
        this.comment = comment;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
