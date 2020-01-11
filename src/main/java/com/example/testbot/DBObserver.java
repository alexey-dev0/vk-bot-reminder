package com.example.testbot;

import com.example.testbot.models.Reminder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DBObserver {
    public void run() {
        var reminders = VKServer.reminderService.findAllReminders();
        for (var r : reminders) {
            if (Timestamp.valueOf(LocalDateTime.now()).after(r.getAppointmentDate())) {
                new VKManager().sendMessage(r.getMessage(), r.getUserId());
                VKServer.reminderService.deleteReminder(r);
            }
        }
    }
}
