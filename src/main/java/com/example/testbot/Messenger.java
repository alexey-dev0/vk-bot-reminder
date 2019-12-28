package com.example.testbot;

import com.vk.api.sdk.objects.messages.Message;

public class Messenger implements Runnable {

    private Message message;

    Messenger(Message message) {
        this.message = message;
    }

    @Override
    public void run() {
        Commander.execute(message);
    }
}