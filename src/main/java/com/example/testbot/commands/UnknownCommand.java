package com.example.testbot.commands;

import com.example.testbot.VKManager;

import com.vk.api.sdk.objects.messages.Message;

public class UnknownCommand extends Command {

    public UnknownCommand(String name) {
        super(name);
    }


    private boolean emptyBody(String body) {
        return body == null || body.isEmpty();
    }


    @Override
    public void exec(Message message) {
        VKManager.sendMessage(
                emptyBody(message.getText())
                        ? "Неизвестная команда"
                        : message.getText(), message.getPeerId()
        );
    }

}