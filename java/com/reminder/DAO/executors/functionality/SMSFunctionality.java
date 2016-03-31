package com.reminder.DAO.executors.functionality;

import com.reminder.DAO.objects.SMSReminder;

import java.util.List;

/**
 * Created by Armen on 22.03.2016.
 */
public interface SMSFunctionality {
    int addSmsReminder(SMSReminder r);
    void editSmsReminder(SMSReminder r);
    void deleteSmsReminder(int id);
    List<SMSReminder> getAllSmsReminders();
}
