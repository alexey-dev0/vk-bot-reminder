package com.example.testbot;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

import java.util.Random;
import java.util.logging.Logger;

public class VKManager {

    private static VKCore vkCore;
    private static final Logger Log = Logger.getLogger(VKManager.class.getName());
    private static final Random random = new Random();


    static {
        try {
            vkCore = new VKCore();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }


    public static void sendMessage(String msg, int peerId) {
        if (msg == null) {
            Log.warning("Сообщение пустое");
            return;
        }
        try {
            Log.info("Отправка сообщения (".concat(msg).concat(")"));
            var sentResult = vkCore.getVk()
                    .messages()
                    .send(vkCore.getActor())
                    .peerId(peerId)
                    .message(msg)
                    .randomId(getRandomId())
                    .execute();

            Log.info("Сообщение отправлено (".concat(sentResult.toString()).concat(")"));
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
            Log.warning("Ошибка при отправке: ".concat(e.getMessage()));
        }
    }


    public static String getUserName(int userId) {
        var result = "";
        try {
            result = vkCore.getVk()
                    .users()
                    .get(vkCore.getActor())
                    .userIds(String.valueOf(userId))
                    .execute()
                    .get(0).getFirstName();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static int getRandomId() {
        var millis = System.currentTimeMillis();
        var randomLong = random.nextLong();
        return (int) (millis + randomLong);
    }

}