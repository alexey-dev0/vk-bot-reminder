package com.example.testbot;

import com.example.testbot.services.ReminderService;
import com.example.testbot.utils.YaSpeechKitUtil;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.enums.DocsType;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.messages.MessageAttachmentType;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import static com.example.testbot.utils.HttpHelper.downloadFile;

public class VKServer {

    private static final Random random = new Random();
    public static ReminderService reminderService;
    private static VKCore vkCore;
    private static DBObserver dbObserver;

    static {
        reminderService = new ReminderService();
    }

    static {
        try {
            vkCore = new VKCore();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }

        dbObserver = new DBObserver();
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

    public static void sendVoiceMessage(String message, int userId) {
        try {
            var vk = vkCore.getVk();
            var actor = vkCore.getActor();

            var ans = vk.docs()
                    .getMessagesUploadServer(actor)
                    .type(DocsType.AUDIO_MESSAGE)
                    .peerId(userId)
                    .execute();
            System.out.println("returned uploadUrl");

            var uploadedFile = vk.upload()
                    .doc(ans.getUploadUrl().toString(), YaSpeechKitUtil.Synthesis(message))
                    .execute();
            System.out.println("file uploaded");

            var response = vk.docs()
                    .save(actor, uploadedFile.getFile())
                    .execute();
            System.out.println("doc saved");

            var sentResult = vk.messages()
                    .send(actor)
                    .attachment("audio_message" + response.getAudioMessage().getOwnerId() + "_" + response.getAudioMessage().getId())
                    .randomId(getRandomId())
                    .peerId(userId)
                    .message(message)
                    .execute();
            System.out.println("message sent");

            System.out.println(sentResult);

        } catch (ApiException | ClientException | InterruptedException | ExecutionException | TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] getVoiceAttachment(Message message) throws InterruptedException, ExecutionException, TimeoutException, URISyntaxException {
        for (var a : message.getAttachments()) {
            if (a.getType() == MessageAttachmentType.AUDIO_MESSAGE) {
                return downloadFile(a.getAudioMessage().getLinkOgg());
            }
        }
        return null;
    }

    public static String RecognizeAudioMessage(byte[] bytes) throws InterruptedException, ExecutionException, TimeoutException {
        return YaSpeechKitUtil.Recognition(bytes);
    }

    public static void main(String[] args) {
        while (true) {
            try {
                // Пауза между запросами
                Thread.sleep(500);

                // Получаем ответ и если есть сообщение обрабатываем
                var messages = vkCore.getMessages();
                if (!messages.isEmpty()) {
                    System.out.println("New msg!");
                    for (var message : messages) {
                        Executors.newCachedThreadPool()
                                .execute(new Messenger(message));
                    }
                }

                // Осматриваем БД на случай наступления времени напоминания
                dbObserver.run();

            } catch (ClientException | InterruptedException | ApiException e) {
                System.out.println("Возникли проблемы: " + e.getMessage());
                final int RECONNECT_TIME = 10000;
                System.out.println("Повторное соединение через " + RECONNECT_TIME / 1000 + " секунд");
                try {
                    Thread.sleep(RECONNECT_TIME);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    return;
                }
            }
        }
    }
}