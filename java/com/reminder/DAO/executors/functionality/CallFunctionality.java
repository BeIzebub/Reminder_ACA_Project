package com.reminder.DAO.executors.functionality;

import com.reminder.DAO.objects.CallReminder;

import java.util.List;

/**
 * Created by Armen on 22.03.2016.
 */
public interface CallFunctionality {
    void addCallReminder(CallReminder r);
    void editCallReminder(CallReminder r);
    void deleteCallReminder(int id);
    List<CallReminder> getAllCallReminders();
}
