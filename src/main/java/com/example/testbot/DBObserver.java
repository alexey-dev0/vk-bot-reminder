package com.example.testbot;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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
