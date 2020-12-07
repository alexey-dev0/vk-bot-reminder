package com.example.testbot;

import com.example.testbot.commands.Command;
import com.example.testbot.commands.UnknownCommand;
import com.vk.api.sdk.objects.messages.Message;

import java.util.Collection;
import java.util.logging.Logger;

class CommandDeterminant {

    private static final Logger Log = Logger.getLogger(CommandDeterminant.class.getName());

    static Command getCommand(Collection<Command> commands, Message message) {

        String body = message.getText();
        Log.info("Новое сообщение (".concat(body).concat(")"));

        for (Command command : commands) {
            if (command.check(body))
                return command;
        }

        return new UnknownCommand("unknown");
    }

}