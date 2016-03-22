package com.reminder.DAO.objects;


/**
 * Created by Armen on 21.03.2016.
 */
public class Reminder {
    protected int id;
    protected String name;
    protected long timeInMillis;
    protected String comment;

    public Reminder() {

    }

    public Reminder(String name, String comment, long time) {
        this.name = name;
        this.timeInMillis = time;
        this.comment = comment;
    }

    public Reminder(int id,String name, String comment, long time) {
        this.id = id;
        this.name = name;
        this.timeInMillis = time;
        this.comment = comment;
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
}
