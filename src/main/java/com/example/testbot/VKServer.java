package com.example.testbot;

import com.example.testbot.models.Reminder;
import com.example.testbot.services.ReminderService;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;

import java.sql.Timestamp;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VKServer {

    public static ReminderService reminderService;

    static {
        reminderService = new ReminderService();
    }

    private static VKCore vkCore;

    static {
        try {
            vkCore = new VKCore();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

    private static DBObserver dbObserver;

    static {
        dbObserver = new DBObserver();
    }

    public static void main(String[] args) {
        while (true) {
            try {
                // Пауза между запросами
                Thread.sleep(300);

                // Получаем ответ и если есть сообщение обрабатываем
                Message message = vkCore.getMessage();
                if (message != null) {
                    ExecutorService exec = Executors.newCachedThreadPool();
                    exec.execute(new Messenger(message));
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