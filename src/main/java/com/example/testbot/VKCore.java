package com.example.testbot;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

class VKCore {

    private static int ts;
    private static int maxMsgId = -1;
    private VkApiClient vk;
    private GroupActor actor;

    VKCore() throws ClientException, ApiException {
        // Инициализация клиента для работы с вк
        TransportClient transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);

        // Загрузка конфигураций
        Properties prop = new Properties();
        int groupId;
        String accessToken;
        try {
            prop.load(new FileInputStream("src/main/resources/app.properties"));
            groupId = Integer.parseInt(prop.getProperty("vk.groupId"));
            accessToken = prop.getProperty("vk.accessToken");
            actor = new GroupActor(groupId, accessToken);
            ts = vk.messages()
                    .getLongPollServer(actor)
                    .execute()
                    .getTs();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при загрузке файла конфигурации");
        }
    }

    GroupActor getActor() {
        return actor;
    }

    VkApiClient getVk() {
        return vk;
    }

    List<Message> getMessages() throws ClientException, ApiException {

        MessagesGetLongPollHistoryQuery eventsQuery = vk.messages()
                .getLongPollHistory(actor)
                .ts(ts);

        if (maxMsgId > 0) eventsQuery.maxMsgId(maxMsgId);

        List<Message> messages = eventsQuery.execute()
                .getMessages()
                .getItems();

        if (!messages.isEmpty()) {
            ts = vk.messages()
                    .getLongPollServer(actor)
                    .execute()
                    .getTs();

            messages.removeIf(x -> x.getFromId() < 0);
            messages.forEach(x -> maxMsgId = Math.max(maxMsgId, x.getId()));
        }
        return messages;
    }
}