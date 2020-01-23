package com.example.testbot.commands;

import com.example.testbot.VKManager;
import com.example.testbot.VKServer;
import com.vk.api.sdk.objects.messages.Message;

public class ListCommand extends Command {
    public ListCommand(String name) {
        super(name);
    }

    @Override
    public boolean check(String message) {
        var lowered = message.toLowerCase();
        return lowered.contains("список") || lowered.contains("напоминания");
    }

    @Override
    public void exec(Message message) {
        int n = 0;
        var sb = new StringBuilder();
        var reminders = VKServer.reminderService.findAllReminders();
        for (var r : reminders) {
            n++;
            if (n == 1) sb.append("Ваши напоминания:\n");
            sb.append(n).append(") ").append(r.getAppointmentDate()).append(": ").append(r.getMessage());
        }
        if (n == 0) sb.append("У вас нет напоминаний");

        new VKManager().sendMessage(sb.toString(), message.getPeerId());
    }
}
