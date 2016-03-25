package com.reminder.DAO.objects;

import java.io.Serializable;

/**
 * Created by Armen on 21.03.2016.
 */
public class SMSReminder extends Reminder implements Serializable {
    private String receiver;
    private String text;

    public SMSReminder() {
        super();
    }

    public SMSReminder(int id, String comment, long time, String receiver, String text) {
        super(id,"SMS to " + receiver, comment, time, Reminder.SMS_REMINDER);
        this.receiver = receiver;
        this.text = text;
    }

    public SMSReminder(String comment, long time, String receiver, String text) {
        super("SMS to " + receiver, comment, time, Reminder.SMS_REMINDER);
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
