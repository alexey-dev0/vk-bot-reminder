package com.example.testbot;

import com.example.testbot.commands.Command;
import com.example.testbot.commands.UnknownCommand;
import com.vk.api.sdk.objects.messages.Message;

import java.util.Collection;

class CommandDeterminant {

    static Command getCommand(Collection<Command> commands, Message message) {
        String body = message.getBody();
        for (Command command : commands) {
            if (command.check(body)) return command;
        }
        return new UnknownCommand("unknown");
    }
}