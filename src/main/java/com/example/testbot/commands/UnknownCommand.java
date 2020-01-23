package com.example.testbot.commands;

import com.example.testbot.VKServer;
import com.vk.api.sdk.objects.messages.Message;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class UnknownCommand extends Command {

    public UnknownCommand(String name) {
        super(name);
    }

    private boolean emptyBody(String body) {
        return body == null || body.isEmpty();
    }

    @Override
    public void exec(Message message) {
        System.out.println(message.getPeerId());
        System.out.println(message.getId());
        System.out.println(message.getFromId());
        System.out.println(message.getAdminAuthorId());
        System.out.println(message.getConversationMessageId());
        System.out.println("Unknown command");
        VKServer.sendVoiceMessage(emptyBody(message.getText()) ? "Неизвестная команда" : message.getText(),
                message.getPeerId());
        System.out.println("voice out");
        try {
            VKServer.getVoiceAttachment(message);
        } catch (InterruptedException | ExecutionException | TimeoutException | URISyntaxException e) {
            e.printStackTrace();
        }
//        new VKManager().sendMessage(
//                emptyBody(message.getBody()) ? "Неизвестная команда" : message.getBody(),
//                message.getUserId());
    }
}