package com.reminder.DAO.objects;


/**
 * Created by Armen on 21.03.2016.
 */
public class Reminder {
    protected int id;
    protected long timeInMillis;
    protected String comment;

    public Reminder() {

    }

    public Reminder(int id, long time, String comment) {
        this.id = id;
        this.timeInMillis = time;
        this.comment = comment;
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
}
