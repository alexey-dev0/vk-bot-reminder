package com.example.testbot;

import com.example.testbot.commands.*;

import java.util.HashSet;

public class CommandManager {

    private static final HashSet<Command> commands = new HashSet<>();


    static {
        commands.add(new UnknownCommand("unknown"));
        commands.add(new GreetingCommand("hello"));
        commands.add(new ReminderCommand("reminder"));
        commands.add(new ListCommand("rlist"));
    }


    static HashSet<Command> getCommands() {
        return commands;
    }

}