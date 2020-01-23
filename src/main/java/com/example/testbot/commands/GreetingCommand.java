package com.example.testbot.commands;

import com.example.testbot.VKServer;
import com.vk.api.sdk.objects.messages.Message;

public class GreetingCommand extends Command {

    public GreetingCommand(String name) {
        super(name);
    }

    @Override
    public boolean check(String message) {
        var greetWords = new String[]{
                "привет",
                "здравствуй",
                "приветствую",
                "здравствуйте",
                "салют",
                "hello",
                "hi",
                "здарова",
                "ку",
                "qq"
        };
        System.out.println(message);
        for (var word : message.toLowerCase()
                .replaceAll("[,.]", "")
                .split(" ")) {
            for (var greet : greetWords)
                if (word.equals(greet)) return true;
        }
        return false;
    }

    @Override
    public void exec(Message message) {
        VKServer.sendVoiceMessage("Привет, "
                        + VKServer.getUserName(message.getPeerId()) + "!",
                message.getPeerId());
        //new VKManager().sendMessage("Привет, " + message.getUserId(), message.getUserId());
    }
}
