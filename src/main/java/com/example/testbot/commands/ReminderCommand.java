package com.example.testbot.commands;

import com.example.testbot.VKManager;
import com.example.testbot.VKServer;
import com.example.testbot.models.Reminder;
import com.vk.api.sdk.objects.messages.Message;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReminderCommand extends Command {

    private static final Logger Log = Logger.getLogger(ReminderCommand.class.getName());
    public ReminderCommand(String name) {
        super(name);
    }


    @Override
    public boolean check(String message) {
        return message.split(" ")[0].toLowerCase().equals("напомни");
    }


    private Timestamp makeTimestamp(int year, int month, int day, int hour, int minute) {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return new Timestamp(cal.getTimeInMillis());
    }


    private void addNewReminder(Message message) throws IllegalArgumentException {
        final Pattern DATE_PATTERN = Pattern.compile("(([0-3][0-9]|[0-9])\\.([01][0-9]|[1-9])(.2[0-9]([2-9][0-9]|19))?)");
        final Pattern TIME_PATTERN = Pattern.compile("([0-1][0-9]|2[0-3]|[0-9]):[0-5][0-9]");

        String content = message.getText();

        int msgIndex = 0;
        Matcher matcherDate = DATE_PATTERN.matcher(content);
        String temp_date = null;
        if (matcherDate.find()) {
            temp_date = matcherDate.group();
            msgIndex = matcherDate.end();
        }

        int day, month, year;
        if (temp_date == null) {

            day = LocalDateTime.now().getDayOfMonth();
            month = LocalDateTime.now().getMonthValue();
            year = LocalDateTime.now().getYear();

        } else {

            String[] date_words = temp_date.split("[.]");

            day = Integer.parseInt(date_words[0]);
            month = Integer.parseInt(date_words[1]);

            if (date_words.length > 2)
                year = Integer.parseInt(date_words[2]);
            else
                year = LocalDateTime.now().getYear();

        }


        Matcher matcherTime = TIME_PATTERN.matcher(content);
        String temp_time = "10:00";
        if (matcherTime.find()) {

            temp_time = matcherTime.group();
            msgIndex = Math.max(matcherTime.end(), msgIndex);

        }

        String[] time_words = temp_time.split(":");
        int hour = Integer.parseInt(time_words[0]);
        int minute = Integer.parseInt(time_words[1]);

        Timestamp appointmentDate = makeTimestamp(year, month, day, hour, minute);
        String msg = content.substring(msgIndex + 1);

        var newReminder = new Reminder(appointmentDate, message.getPeerId(), msg);

        Log.info("Добавление напоминания в БД...");
        VKServer.reminderService.saveReminder(newReminder);
    }


    @Override
    public void exec(Message message) {

        String answer;

        try {

            Log.info("Создание нового напоминания...");

            addNewReminder(message);

            Log.info("Новое напоминание создано");
            answer = "Новое напоминание успешно создано.";

        } catch (IllegalArgumentException e) {

            e.printStackTrace();
            answer = "Неверный формат даты. Попробуйте ещё раз.";

        }

        VKManager.sendMessage(answer, message.getPeerId());

    }
}
