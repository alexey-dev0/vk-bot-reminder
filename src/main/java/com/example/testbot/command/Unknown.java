package com.example.testbot.command;

import com.example.testbot.VKManager;
import com.vk.api.sdk.objects.messages.Message;

public class Unknown extends Command {

    public Unknown(String name) {
        super(name);
    }

    private boolean emptyBody(String body) {
        return body == null || body.isEmpty();
    }

    @Override
    public void exec(Message message) {
        new VKManager().sendMessage(
                emptyBody(message.getBody()) ? "Неизвестная команда" : message.getBody(),
                message.getUserId());
    }
}