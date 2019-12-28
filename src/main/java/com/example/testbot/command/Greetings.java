package com.example.testbot.command;

import com.example.testbot.VKManager;
import com.vk.api.sdk.objects.messages.Message;

import java.util.List;

public class Greetings extends Command {

    public Greetings(String name) {
        super(name);
    }

    @Override
    public boolean check(String message) {
        var greetWords = new String[] {
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
        new VKManager().sendMessage("Привет, " + message.getUserId(), message.getUserId());
    }
}
