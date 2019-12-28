package com.example.testbot;

import com.example.testbot.command.Command;
import com.example.testbot.command.Unknown;
import com.vk.api.sdk.objects.messages.Message;

import java.util.Collection;

class CommandDeterminant {

    static Command getCommand(Collection<Command> commands, Message message) {
        String body = message.getBody();
        for (Command command : commands) {
            if (command.check(body)) return command;
        }
        return new Unknown("unknown");
    }
}