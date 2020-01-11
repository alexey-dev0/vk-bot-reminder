package com.example.testbot.services;

import com.example.testbot.dao.ReminderDAO;
import com.example.testbot.models.Reminder;

import java.util.List;

public class ReminderService {
    private ReminderDAO reminderDao = new ReminderDAO();

    public ReminderService() {
    }

    public Reminder saveReminder(int id) {
        return reminderDao.findById(id);
    }

    public void saveReminder(Reminder reminder) {
        reminderDao.save(reminder);
    }

    public void deleteReminder(Reminder reminder) {
        reminderDao.delete(reminder);
    }

    public void updateReminder(Reminder reminder) {
        reminderDao.update(reminder);
    }

    public List<Reminder> findAllReminders() {
        return reminderDao.findAll();
    }
}
