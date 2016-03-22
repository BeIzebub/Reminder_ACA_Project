package com.reminder.DAO.objects;

/**
 * Created by Armen on 21.03.2016.
 */
public class SMSReminder extends Reminder {
    private String receiver;
    private String text;

    public SMSReminder() {
        super();
    }

    public SMSReminder(int id, long time, String comment, String receiver, String text) {
        super(id, time, comment);
        this.receiver = receiver;
        this.text = text;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
