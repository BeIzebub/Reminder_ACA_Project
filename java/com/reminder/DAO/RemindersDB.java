package com.reminder.DAO;

import android.content.Context;

import com.reminder.DAO.executors.CallQueries;
import com.reminder.DAO.executors.ReminderQueries;
import com.reminder.DAO.executors.SMSQueries;
import com.reminder.DAO.executors.functionality.CallFunctionality;
import com.reminder.DAO.executors.functionality.ReminderFunctionality;
import com.reminder.DAO.executors.functionality.SMSFunctionality;
import com.reminder.DAO.objects.CallReminder;
import com.reminder.DAO.objects.Reminder;
import com.reminder.DAO.objects.SMSReminder;

import java.util.List;

/**
 * Created by Armen on 22.03.2016.
 */
public class RemindersDB implements ReminderFunctionality, CallFunctionality, SMSFunctionality {

    private static ReminderQueries rq;
    private static CallQueries cq;
    private static SMSQueries sq;

    private static RemindersDB db;

    private RemindersDB(Context context) {
        rq = new ReminderQueries(context);
        cq = new CallQueries(context);
        sq = new SMSQueries(context);
    }

    public static RemindersDB getInstance(Context context) {
        if (db == null)
            db = new RemindersDB(context);
        return db;
    }

    @Override
    public void addCallReminder(CallReminder r) {
        cq.open();
        cq.addCallReminder(r);
        cq.close();
    }

    @Override
    public void editCallReminder(CallReminder r) {
        cq.open();
        cq.editCallReminder(r);
        cq.close();
    }

    @Override
    public void deleteCallReminder(int id) {
        cq.open();
        cq.deleteCallReminder(id);
        cq.close();
    }

    @Override
    public List<CallReminder> getAllCallReminders() {
        cq.open();
        List<CallReminder> reminders = cq.getAllCallReminders();
        cq.close();
        return reminders;
    }

    @Override
    public int addReminder(Reminder r) {
        rq.open();
        int id = rq.addReminder(r);
        rq.close();
        return id;
    }

    @Override
    public void editReminder(Reminder r) {
        rq.open();
        rq.editReminder(r);
        rq.close();
    }

    @Override
    public void deleteReminder(int id) {
        rq.open();
        rq.deleteReminder(id);
        rq.close();
    }

    @Override
    public List<Reminder> getAllReminders() {
        rq.open();
        List<Reminder> reminders = rq.getAllReminders();
        rq.close();
        return reminders;
    }

    @Override
    public void addSmsReminder(SMSReminder r) {
        sq.open();
        sq.addSmsReminder(r);
        sq.close();
    }

    @Override
    public void editSmsReminder(SMSReminder r) {
        sq.open();
        sq.editSmsReminder(r);
        sq.close();
    }

    @Override
    public void deleteSmsReminder(int id) {
        sq.open();
        sq.deleteSmsReminder(id);
        sq.close();
    }

    @Override
    public List<SMSReminder> getAllSmsReminders() {
        sq.open();
        List<SMSReminder> reminders = sq.getAllSmsReminders();
        sq.close();
        return reminders;
    }
}
