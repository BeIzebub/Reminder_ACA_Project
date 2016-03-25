package com.reminder.DAO.objects;

/**
 * Created by Armen on 21.03.2016.
 */
public class CallReminder extends Reminder {
    private String receiver;

    public CallReminder() {
        super();
    }

    public CallReminder(int id, String comment, long time, String receiver) {
        super(id,"Call to " + receiver, comment, time, Reminder.CALL_REMINDER);
        this.receiver = receiver;
    }

    public CallReminder(String comment, long time, String receiver) {
        super("Call to " + receiver, comment, time, Reminder.CALL_REMINDER);
        this.receiver = receiver;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
