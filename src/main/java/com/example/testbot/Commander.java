package com.example.testbot;

import com.vk.api.sdk.objects.messages.Message;

class Commander {

    /**
     * Обработка сообщений, получаемых через сервис ВКонтакте. Имеет ряд дополнительной информации.
     *
     * @param message сообщение (запрос) пользователя
     */
    static void execute(Message message) {
        CommandDeterminant.getCommand(CommandManager.getCommands(), message).exec(message);
    }
}
