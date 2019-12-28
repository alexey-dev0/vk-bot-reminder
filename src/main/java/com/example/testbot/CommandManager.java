package com.example.testbot;

import com.example.testbot.command.Command;
import com.example.testbot.command.Greetings;
import com.example.testbot.command.Reminder;
import com.example.testbot.command.Unknown;

import java.util.HashSet;

public class CommandManager {
    private static HashSet<Command> commands = new HashSet<>();

    static {
        commands.add(new Unknown("unknown"));
        commands.add(new Greetings("hello"));
        commands.add(new Reminder("reminder"));
    }

    static HashSet<Command> getCommands() {
        return commands;
    }

    public static void addCommand(Command command) {
        commands.add(command);
    }
}