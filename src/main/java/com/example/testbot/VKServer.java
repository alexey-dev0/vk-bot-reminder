package com.example.testbot;

import com.example.testbot.services.ReminderService;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

import java.util.concurrent.Executors;
import java.util.logging.Logger;


public class VKServer {

    public static ReminderService reminderService;
    private static VKCore vkCore;
    private static final DBObserver dbObserver;
    private static final Logger Log = Logger.getLogger(VKServer.class.getName());

    static {

        Log.info("Запуск сервиса напоминаний");
        reminderService = new ReminderService();
        Log.info("Готово");

        Log.info("Запуск ядра VK");
        try {
            vkCore = new VKCore();
            Log.info("Готово");
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }

        Log.info("Запуск слушателя БД");
        dbObserver = new DBObserver();
        Log.info("Готово");
    }


    public static void main(String[] args) {
        while (true) {
            try {
                // Пауза между запросами
                Thread.sleep(500);

                // Получаем ответ и если есть сообщение обрабатываем
                var messages = vkCore.getMessages();
                if (!messages.isEmpty()) {
                    for (var message : messages) {
                        Executors.newCachedThreadPool()
                                .execute(new Messenger(message));
                    }
                }

                // Осматриваем БД на случай наступления времени напоминания
                dbObserver.run();

            } catch (ClientException | InterruptedException | ApiException e) {
                Log.warning("Возникли проблемы: " + e.getMessage());

                final int RECONNECT_TIME = 10000;
                Log.warning("Повторное соединение через " + RECONNECT_TIME / 1000 + " секунд");

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