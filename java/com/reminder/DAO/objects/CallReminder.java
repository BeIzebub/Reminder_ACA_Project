package com.reminder.DAO.objects;

/**
 * Created by Armen on 21.03.2016.
 */
public class CallReminder extends Reminder {
    private String receiver;

    public CallReminder() {
        super();
    }

    public CallReminder(int id, long time, String comment, String receiver) {
        super(id, time, comment);
        this.receiver = receiver;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
