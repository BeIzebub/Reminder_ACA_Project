package com.reminder.DAO.executors.functionality;

import com.reminder.DAO.objects.Reminder;

import java.util.List;

/**
 * Created by Armen on 22.03.2016.
 */
public interface ReminderFunctionality {
    int addReminder(Reminder r);
    void editReminder(Reminder r);
    void deleteReminder(int id);
    List<Reminder> getAllReminders();
    List<Reminder> getAllSimpleReminders();
    List<Reminder> getAllFacebookReminders();
}
