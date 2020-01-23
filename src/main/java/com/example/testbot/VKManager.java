package com.example.testbot;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;

public class VKManager {

    private static VKCore vkCore;

    static {
        try {
            vkCore = new VKCore();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * Обращается к VK API и получает объект, описывающий пользователя.
     *
     * @param id идентификатор пользователя в VK
     * @return {@link UserXtrCounters} информацию о пользователе
     * @see UserXtrCounters
     */
    public static UserXtrCounters getUserInfo(int id) {
        try {
            return vkCore.getVk().users()
                    .get(vkCore.getActor())
                    .userIds(String.valueOf(id))
                    .execute()
                    .get(0);
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendMessage(String msg, int peerId) {
        if (msg == null) {
            System.out.println("null");
            return;
        }
        try {
            vkCore.getVk()
                    .messages()
                    .send(vkCore.getActor())
                    .peerId(peerId)
                    .message(msg)
                    .randomId(VKServer.getRandomId())
                    .execute();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

    public MessagesSendQuery getSendQuery() {
        return vkCore.getVk().messages().send(vkCore.getActor());
    }
}